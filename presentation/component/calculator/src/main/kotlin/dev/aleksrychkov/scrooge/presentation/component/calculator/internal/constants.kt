@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

internal const val DIVIDE = '÷'
internal const val DIVIDE_STRING = "÷"
internal const val MULTIPLY = '×'
internal const val MULTIPLY_STRING = "×"
internal const val SUBTRACT = '-'
internal const val SUBTRACT_STRING = "-"
internal const val ADD = '+'
internal const val ADD_STRING = "+"
internal const val CLEAN_STRING = "AC"
internal const val OPEN_PARENTHESES = '('
internal const val OPEN_PARENTHESES_STRING = "("
internal const val CLOSE_PARENTHESES = ')'
internal const val CLOSE_PARENTHESES_STRING = ")"
internal const val DECIMAL_SEPARATOR = '.'

fun Char.isOperand(): Boolean =
    this == ADD || this == SUBTRACT || this == DIVIDE || this == MULTIPLY

fun Char.isParentheses(): Boolean =
    this == OPEN_PARENTHESES || this == CLOSE_PARENTHESES
