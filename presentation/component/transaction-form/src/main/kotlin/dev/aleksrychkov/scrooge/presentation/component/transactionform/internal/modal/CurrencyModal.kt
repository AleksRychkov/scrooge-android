package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyModal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.TransactionFormComponentInternal

@Composable
internal fun FromCurrencyModal(
    component: TransactionFormComponentInternal,
) {
    val currencySlot = component.currencyModal.subscribeAsState().value
    FromCurrencyModal(
        slot = currencySlot,
        close = component::closeCurrencyModal,
        select = component::selectCurrency,
    )
}

@Composable
private fun FromCurrencyModal(
    slot: ChildSlot<*, CurrencyComponent>,
    close: () -> Unit,
    select: (CurrencyEntity) -> Unit,
) {
    slot.child?.instance?.also { component ->
        CurrencyModal(
            component = component,
            onDismiss = close,
            callback = select,
        )
    }
}
