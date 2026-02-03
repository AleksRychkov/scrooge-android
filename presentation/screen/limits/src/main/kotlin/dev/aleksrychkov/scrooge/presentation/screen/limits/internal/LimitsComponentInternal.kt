package dev.aleksrychkov.scrooge.presentation.screen.limits.internal

import dev.aleksrychkov.scrooge.presentation.screen.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal interface LimitsComponentInternal : LimitsComponent {
    val state: StateFlow<LimitsState>

    fun onBackPressed()
    fun onSaveClicked()
    fun onAddLimitClicked()
    fun onAmountChanged(id: Long, value: String)
    fun onDeleteLimitClicked(id: Long)
}
