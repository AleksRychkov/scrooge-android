package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyModal
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.CurrencySlotDto
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.LimitsComponentInternal

@Composable
internal fun LimitsCurrencyModal(
    component: LimitsComponentInternal,
) {
    val currencySlot = component.currencyModal.subscribeAsState().value

    LimitsCurrencyModal(
        slot = currencySlot,
        close = component::closeCurrencyModal,
        select = component::selectCurrency,
    )
}

@Composable
private fun LimitsCurrencyModal(
    slot: ChildSlot<*, CurrencySlotDto>,
    close: () -> Unit,
    select: (Long, CurrencyEntity) -> Unit,
) {
    slot.child?.instance?.also { dto ->
        CurrencyModal(
            component = dto.component,
            onDismiss = close,
            callback = {
                select(dto.limitId, it)
            },
        )
    }
}
