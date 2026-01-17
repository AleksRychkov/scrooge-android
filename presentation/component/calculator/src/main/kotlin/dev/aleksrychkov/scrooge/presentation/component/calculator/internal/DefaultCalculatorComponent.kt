package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class DefaultCalculatorComponent(
    componentContext: ComponentContext,
) : CalculatorComponentInternal, ComponentContext by componentContext {

    private val _state = MutableStateFlow(CalculatorState())

    override val state: StateFlow<CalculatorState>
        get() = _state.asStateFlow()

    override fun onDigitClicked(digit: Int) {
        val state = state.value
        _state.value = state.copy(infix = state.infix + digit.toString())
    }

    override fun onCleanClicked() {
        _state.value = _state.value.copy(infix = "", result = "")
    }

    override fun onOpenParenthesesClicked() {
        val state = state.value
        if (state.infix.canAddOpenParenthesis()) {
            _state.value = state.copy(infix = state.infix + "(")
        }
    }

    override fun onCloseParenthesesClicked() {
        val state = state.value
        if (state.infix.canAddCloseParenthesis()) {
            _state.value = state.copy(infix = state.infix + ")")
        }
    }

    override fun onDivideClicked() {
        val state = state.value
        _state.value = state.copy(infix = state.infix + "÷")
    }

    override fun onMultiplyClicked() {
        val state = state.value
        _state.value = state.copy(infix = state.infix + "×")
    }

    override fun onSubtractClicked() {
        val state = state.value
        _state.value = state.copy(infix = state.infix + "—")
    }

    override fun onAddClicked() {
        val state = state.value
        _state.value = state.copy(infix = state.infix + "+")
    }

    override fun onDecimalClicked() {
        val state = state.value
        _state.value = state.copy(infix = state.infix + AMOUNT_DELIMITER_STRING)
    }

    override fun onRemoveClicked() {
        val state = state.value
        if (state.infix.isNotEmpty()) {
            _state.value = state.copy(infix = state.infix.dropLast(1))
        }
    }

    private fun String.canAddOpenParenthesis(): Boolean =
        this.isEmpty() || this.last().isOperator()

    private fun String.canAddCloseParenthesis(): Boolean =
        this.isNotEmpty() && (this.last().isDigit() || this.last() == ')')

    private fun Char.isOperator(): Boolean =
        this == '÷' || this == '×' || this == '—' || this == '+' || this == '('
}
