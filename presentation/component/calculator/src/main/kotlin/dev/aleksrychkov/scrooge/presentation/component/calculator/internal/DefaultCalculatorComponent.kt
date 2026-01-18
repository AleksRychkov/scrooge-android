package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class DefaultCalculatorComponent(
    componentContext: ComponentContext,
    resourceManager: ResourceManager = get(),
) : CalculatorComponentInternal, ComponentContext by componentContext {

    private companion object {
        const val SCALE = 2
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
        retainedCoroutineScope().launch(Dispatchers.IO) {
            runSuspendCatching {
                val result = calculator.calculate(infix)
                _state.value = _state.value.copy(
                    result = result
                        .setScale(SCALE, RoundingMode.HALF_EVEN)
                        .toString()
                        .replace(
                            DECIMAL_SEPARATOR,
                            AMOUNT_DELIMITER
                        ),
                    errorMessage = null,
                )
            }.onFailure { e ->
                val msg = if (e.message?.contains("divide by zero") == true) {
                    divisionByZeroErrorMessage
                } else {
                    invalidInfixInputErrorMessage
                }
                _state.value = _state.value.copy(result = "", errorMessage = msg)
            }
        }
    }
}
