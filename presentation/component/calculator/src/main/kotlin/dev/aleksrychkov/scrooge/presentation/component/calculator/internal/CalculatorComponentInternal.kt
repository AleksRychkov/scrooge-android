package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import dev.aleksrychkov.scrooge.presentation.component.calculator.CalculatorComponent
import kotlinx.coroutines.flow.StateFlow

internal interface CalculatorComponentInternal : CalculatorComponent {
    val state: StateFlow<CalculatorState>

    fun calculateResult(infix: String)
}
