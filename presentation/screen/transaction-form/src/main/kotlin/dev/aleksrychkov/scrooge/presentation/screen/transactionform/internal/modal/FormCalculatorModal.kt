package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentation.component.calculator.CalculatorModal
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal

@Composable
internal fun FormCalculatorModal(
    component: TransactionFormComponentInternal,
) {
    val slot = component.calculatorModal.subscribeAsState().value
    CalculatorModal(
        slot = slot,
        close = component::closeCalculatorModal,
        callback = component::setAmount,
    )
}
