package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import androidx.compose.runtime.Immutable

@Immutable
internal data class CalculatorState(
    val infix: String = "",
    val result: String = "",
)
