package dev.aleksrychkov.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InternalClassRuleTest {

    private val rule = InternalClassRule()

    @Test
    fun `When public class is in internal package Then it is reported`() {
        // Given
        val code = """
            class Foo
        """.trimIndent()
        rule.testPackageName = "package some.feature.internal"

        // When
        val findings = rule.compileAndLint(code)

        // Then
        assertEquals(1, findings.size)
        val finding = findings.first() as CodeSmell
        assert(finding.message.contains("Foo"))
    }

    @Test
    fun `When class is internal in internal package Then it is not reported`() {
        // Given
        val code = """
            internal class Foo
        """.trimIndent()
        rule.testPackageName = "package some.feature.internal"

        // When
        val findings = rule.compileAndLint(code)

        // Then
        assertEquals(0, findings.size)
    }

    @Test
    fun `When class is in non-internal package Then it is not reported`() {
        // Given
        val code = """
            class Foo
        """.trimIndent()
        rule.testPackageName = "package some.feature.public"

        // When
        val findings = rule.compileAndLint(code)

        // Then
        assertEquals(0, findings.size)
    }
}
