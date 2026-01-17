package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class DefaultCalculatorComponent(
    componentContext: ComponentContext,
) : CalculatorComponentInternal, ComponentContext by componentContext {

    private val _state = MutableStateFlow(CalculatorState())

    override val state: StateFlow<CalculatorState>
        get() = _state.asStateFlow()
}
