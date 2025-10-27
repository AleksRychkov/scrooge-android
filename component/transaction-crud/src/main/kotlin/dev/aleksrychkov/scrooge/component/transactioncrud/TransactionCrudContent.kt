package dev.aleksrychkov.scrooge.component.transactioncrud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.component.transactioncrud.internal.TransactionCrudComponentInternal
import dev.aleksrychkov.scrooge.component.transactioncrud.internal.modal.CategoryModal
import dev.aleksrychkov.scrooge.component.transactioncrud.internal.modal.TagModal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large

@Composable
fun TransactionCrudContent(
    modifier: Modifier,
    component: TransactionCrudComponent
) {
    TransactionCrudContent(
        modifier = modifier,
        component = component as TransactionCrudComponentInternal,
    )
}

@Composable
private fun TransactionCrudContent(
    modifier: Modifier,
    component: TransactionCrudComponentInternal,
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
    }

    CategoryModal(
        component = component,
    )
    TagModal(
        component = component,
    )
}
