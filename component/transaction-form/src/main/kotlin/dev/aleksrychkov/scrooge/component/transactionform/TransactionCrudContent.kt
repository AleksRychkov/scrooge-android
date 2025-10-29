package dev.aleksrychkov.scrooge.component.transactionform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.CategoryModal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.CurrencyModal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.TagModal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large

@Composable
fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponent
) {
    TransactionFormContent(
        modifier = modifier,
        component = component as TransactionFormComponentInternal,
    )
}

@Composable
private fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponentInternal,
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(Large))

        Button(onClick = component::openCategoryModal) {
            Text("Select category")
        }
        Spacer(modifier = Modifier.height(Large))

        Button(onClick = component::openTagModal) {
            Text("Add tag")
        }

        Spacer(modifier = Modifier.height(Large))
        Button(onClick = component::openCurrencyModal) {
            Text("Select currency")
        }
    }

    CategoryModal(
        component = component,
    )
    TagModal(
        component = component,
    )
    CurrencyModal(
        component = component,
    )
}
