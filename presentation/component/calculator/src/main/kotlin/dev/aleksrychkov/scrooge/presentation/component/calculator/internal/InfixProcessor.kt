package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER

@Suppress("LoopWithTooManyJumpStatements", "CyclomaticComplexMethod")
internal fun processNewInfix(current: String, newValue: Char): String {
    val isAllowedToAppend = when {
        newValue == OPEN_PARENTHESES -> {
            current.isEmpty() ||
                current.last() == OPEN_PARENTHESES ||
                current.last().isOperand()
        }

        newValue == CLOSE_PARENTHESES -> {
            fun canAddCloseParentheses(): Boolean {
                var index = current.length - 1
                var openCount = 0
                var closeCount = 0
                while (index >= 0) {
                    val c = current[index]
                    if (c == OPEN_PARENTHESES) openCount++
                    if (c == CLOSE_PARENTHESES) closeCount++
                    index--
                }
                return openCount > closeCount
            }
            current.isNotEmpty() &&
                (current.last() == CLOSE_PARENTHESES || current.last().isDigit()) &&
                canAddCloseParentheses()
        }

        newValue == AMOUNT_DELIMITER -> {
            fun isNoDelimiterInNumber(): Boolean {
                var delimiterCount = 0
                var index = current.length - 1
                while (index >= 0) {
                    val c = current[index]
                    if (c == AMOUNT_DELIMITER) {
                        delimiterCount++
                        break
                    } else if (!c.isDigit()) {
                        break
                    }
                    index--
                }
                return delimiterCount == 0
            }
            current.isNotEmpty() && current.last().isDigit() && isNoDelimiterInNumber()
        }

        newValue.isNatural() -> {
            current.isEmpty() ||
                current.last().isDigit() ||
                current.last() == AMOUNT_DELIMITER ||
                current.last() == OPEN_PARENTHESES ||
                current.last().isOperand()
        }

        newValue == '0' -> {
            fun hasDigitOrDelimiter(): Boolean {
                var index = current.length - 1
                while (index >= 0) {
                    val c = current[index]
                    if (c == AMOUNT_DELIMITER || c.isNatural()) {
                        return true
                    } else if (!c.isDigit()) {
                        break
                    }
                    index--
                }
                return false
            }
            current.isEmpty() ||
                current.last().isNatural() ||
                current.last().isOperand() ||
                current.last() == OPEN_PARENTHESES || hasDigitOrDelimiter()
        }

        newValue.isOperand() -> {
            (current.isEmpty() && newValue == SUBTRACT) ||
                (current.isEmpty() && newValue == ADD) ||
                (current.isNotEmpty() && current.last() == CLOSE_PARENTHESES) ||
                (current.isNotEmpty() && current.last().isDigit()) ||
                (current.isNotEmpty() && current.last() == OPEN_PARENTHESES && newValue == SUBTRACT) ||
                (current.isNotEmpty() && current.last() == OPEN_PARENTHESES && newValue == ADD)
        }

        else -> {
            true
        }
    }

    return if (isAllowedToAppend) current + newValue else current
}
