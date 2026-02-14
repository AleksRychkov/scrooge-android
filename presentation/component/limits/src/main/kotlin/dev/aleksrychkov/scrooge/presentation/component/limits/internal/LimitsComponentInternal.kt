package dev.aleksrychkov.scrooge.presentation.component.limits.internal

import dev.aleksrychkov.scrooge.presentation.component.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal interface LimitsComponentInternal : LimitsComponent {
    val state: StateFlow<LimitsState>
}
