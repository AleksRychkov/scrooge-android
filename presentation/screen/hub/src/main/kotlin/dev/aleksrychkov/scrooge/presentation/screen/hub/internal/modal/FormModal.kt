package dev.aleksrychkov.scrooge.presentation.screen.hub.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.presentation.component.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.TransactionFormModal
import dev.aleksrychkov.scrooge.presentation.screen.hub.internal.HubComponentInternal

@Composable
internal fun FormModal(
    component: HubComponentInternal
) {
    val slot = component.formModal.subscribeAsState().value
    Modal(
        slot = slot,
        onClose = component::closeFormModal,
    )
}

@Composable
private fun Modal(
    slot: ChildSlot<*, TransactionFormComponent>,
    onClose: () -> Unit,
) {
    TransactionFormModal(
        slot = slot,
        onClose = onClose,
    )
}
