package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import androidx.compose.runtime.Immutable

@Immutable
internal data class CalculatorState(
    val infix: String = "3+4*2/(1-5)",
    val result: String = "5",
    val resultRaw: Long = 0L,
)
