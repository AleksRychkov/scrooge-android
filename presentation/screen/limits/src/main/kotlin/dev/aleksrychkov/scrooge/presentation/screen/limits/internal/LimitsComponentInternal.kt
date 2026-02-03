package dev.aleksrychkov.scrooge.presentation.screen.limits.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentation.screen.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal interface LimitsComponentInternal : LimitsComponent {
    val state: StateFlow<LimitsState>
    val currencyModal: Value<ChildSlot<*, CurrencySlotDto>>

    fun onBackPressed()
    fun onAddLimitClicked()
    fun onDeleteLimitClicked(id: Long)
    fun onAmountChanged(id: Long, value: String)
    fun onPeriodChanged(id: Long, period: String)

    fun openCurrencyModal(id: Long)
    fun closeCurrencyModal()
    fun selectCurrency(id: Long, currency: CurrencyEntity)
}

internal class CurrencySlotDto(val component: CurrencyComponent, val limitId: Long)
