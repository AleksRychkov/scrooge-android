package dev.aleksrychkov.scrooge.presentation.component.filters.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyModal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal

@Composable
internal fun FiltersCurrencyModal(
    component: FiltersComponentInternal,
) {
    val slot = component.currencyModal.subscribeAsState().value
    slot.child?.instance?.also { currencyComponent ->
        CurrencyModal(
            component = currencyComponent,
            onDismiss = component::closeCurrencyModal,
            callback = component::setCurrency,
        )
    }
}
