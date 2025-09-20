package dev.aleksrychkov.detektrules

import androidx.annotation.VisibleForTesting
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class InternalClassRule : Rule() {

    @VisibleForTesting
    var testPackageName: String? = null

    override val issue: Issue = Issue(
        id = "InternalPackageVisibility",
        severity = Severity.Style,
        description = "Classes in internal packages should have internal visibility.",
        debt = Debt.FIVE_MINS
    )

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        // Get package name
        val packageName = testPackageName ?: klass.containingKtFile.packageFqName.asString()

        // Only enforce for internal packages
        if ("internal" in packageName.split(".")) {
            val modifierList = klass.modifierList
            val hasInternal =
                modifierList?.hasModifier(org.jetbrains.kotlin.lexer.KtTokens.INTERNAL_KEYWORD)
                    ?: false

            if (!hasInternal) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(klass),
                        message = "Class '${klass.name}' is in internal package" +
                            " '$packageName' and should be 'internal'."
                    )
                )
            }
        }
    }
}
