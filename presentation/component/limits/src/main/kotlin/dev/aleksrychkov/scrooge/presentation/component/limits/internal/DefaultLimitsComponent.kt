package dev.aleksrychkov.scrooge.presentation.component.limits.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class DefaultLimitsComponent(
    componentContext: ComponentContext,
) : LimitsComponentInternal, ComponentContext by componentContext {
    override val state: StateFlow<LimitsState> = MutableStateFlow(LimitsState())
}
