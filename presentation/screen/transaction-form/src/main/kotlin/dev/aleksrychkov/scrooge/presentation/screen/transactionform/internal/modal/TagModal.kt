package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentation.component.tags.TagModal
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal

@Composable
internal fun FormTagModal(
    component: TransactionFormComponentInternal,
) {
    val slot = component.tagModal.subscribeAsState().value
    TagModal(
        slot = slot,
        close = component::closeTagModal,
        select = component::addTag,
    )
}
