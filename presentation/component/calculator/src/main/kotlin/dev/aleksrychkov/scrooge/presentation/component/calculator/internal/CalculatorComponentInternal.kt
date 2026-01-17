package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import dev.aleksrychkov.scrooge.presentation.component.calculator.CalculatorComponent
import kotlinx.coroutines.flow.StateFlow

internal interface CalculatorComponentInternal : CalculatorComponent {
    val state: StateFlow<CalculatorState>

    fun onDigitClicked(digit: Int)
    fun onCleanClicked()
    fun onOpenParenthesesClicked()
    fun onCloseParenthesesClicked()
    fun onDivideClicked()
    fun onMultiplyClicked()
    fun onSubtractClicked()
    fun onAddClicked()
    fun onDecimalClicked()
    fun onRemoveClicked()
}
