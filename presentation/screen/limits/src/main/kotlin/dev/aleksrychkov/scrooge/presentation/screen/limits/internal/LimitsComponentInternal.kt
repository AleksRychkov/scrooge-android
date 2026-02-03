package dev.aleksrychkov.scrooge.presentation.screen.limits.internal

import dev.aleksrychkov.scrooge.presentation.screen.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal interface LimitsComponentInternal : LimitsComponent {
    val state: StateFlow<LimitsState>

    fun onBackPressed()
    fun onAddLimitClicked()
    fun onDeleteLimitClicked(id: Long)
    fun onAmountChanged(id: Long, value: String)
    fun onCurrencyChanged(id: Long, code: String)
    fun onPeriodChanged(id: Long, period: String)
}
