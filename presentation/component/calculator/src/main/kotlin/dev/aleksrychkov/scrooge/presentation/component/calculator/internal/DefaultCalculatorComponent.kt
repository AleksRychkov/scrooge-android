package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.round
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class DefaultCalculatorComponent(
    componentContext: ComponentContext,
    resourceManager: ResourceManager = get(),
) : CalculatorComponentInternal, ComponentContext by componentContext {

    private companion object {
        const val ROUNDING = 100
    }

    private val calculator = InfixCalculator()
    private val invalidInfixInputErrorMessage: String by lazy(mode = LazyThreadSafetyMode.NONE) {
        resourceManager.getString(Resources.string.calculator_invalid_expression_error)
    }
    private val divisionByZeroErrorMessage: String by lazy(mode = LazyThreadSafetyMode.NONE) {
        resourceManager.getString(Resources.string.calculator_division_by_0)
    }

    private val _state = MutableStateFlow(CalculatorState())

    override val state: StateFlow<CalculatorState>
        get() = _state.asStateFlow()

    @Suppress("TooGenericExceptionCaught")
    override fun calculateResult(infix: String) {
        if (infix.isEmpty()) {
            _state.value = _state.value.copy(result = "", errorMessage = null)
            return
        }
        try {
            val result = calculator.calculate(infix)
            if (result == Float.POSITIVE_INFINITY || result == Float.NEGATIVE_INFINITY) {
                _state.value = _state.value.copy(
                    result = "",
                    errorMessage = divisionByZeroErrorMessage,
                )
            } else {
                _state.value = _state.value.copy(
                    result = (round(result * ROUNDING) / ROUNDING)
                        .toString()
                        .replace(
                            DECIMAL_SEPARATOR,
                            AMOUNT_DELIMITER
                        ),
                    errorMessage = null,
                )
            }
        } catch (_: Exception) {
            _state.value =
                _state.value.copy(result = "", errorMessage = invalidInfixInputErrorMessage)
        }
    }
}
