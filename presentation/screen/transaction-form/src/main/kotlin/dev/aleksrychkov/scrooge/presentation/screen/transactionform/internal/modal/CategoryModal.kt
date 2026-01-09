package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryModal
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal

@Composable
internal fun FormCategoryModal(
    component: TransactionFormComponentInternal,
) {
    val slot = component.categoryModal.subscribeAsState().value
    CategoryModal(
        slot = slot,
        close = component::closeCategoryModal,
        select = component::setCategory,
    )
}
