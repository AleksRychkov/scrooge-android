package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import java.util.ArrayDeque

/*
All operations are binary (i.e., they take two arguments),
and all are left-associative (i.e., when priorities are equal, they are evaluated from left to right).
Parentheses are allowed.
 */
internal class InfixCalculator {

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NestedBlockDepth")
    fun calculate(input: String): Float {
        val tmp = input
            .replace(AMOUNT_DELIMITER, DECIMAL_SEPARATOR)
            .replace('*', MULTIPLY)
            .replace('-', SUBTRACT)
            .replace('/', DIVIDE)

        val numberStack = ArrayDeque<Float>()
        val operandsStack = ArrayDeque<Char>()

        var index = 0
        while (index < tmp.length) {
            val current = tmp[index]
            when {
                current.isDigit() -> {
                    var number = "$current"
                    var j = index + 1
                    while (j < tmp.length) {
                        val tmpJ = tmp[j]
                        if (tmpJ.isDigit() || tmpJ == DECIMAL_SEPARATOR) {
                            number += tmpJ
                        } else {
                            break
                        }
                        j++
                    }
                    numberStack.push(number.toFloat())
                    index = j
                }

                current == OPEN_PARENTHESES -> {
                    operandsStack.push(current)
                    index++
                }

                current == CLOSE_PARENTHESES -> {
                    while (operandsStack.peek() != OPEN_PARENTHESES) {
                        processOperation(numberStack, operandsStack.pop())
                    }
                    operandsStack.pop()
                    index++
                }

                current.isOperand() -> {
                    while (operandsStack.isNotEmpty() &&
                        operandsStack.peek().operandPriority() >= current.operandPriority()
                    ) {
                        processOperation(numberStack, operandsStack.pop())
                    }
                    operandsStack.push(current)
                    index++
                }
            }
        }
        while (operandsStack.isNotEmpty()) {
            processOperation(numberStack, operandsStack.pop())
        }
        return numberStack.pop()
    }

    private fun Char.operandPriority(): Int =
        when (this) {
            ADD, SUBTRACT -> 1
            DIVIDE, MULTIPLY -> 2
            else -> -1
        }

    private fun processOperation(stack: ArrayDeque<Float>, operand: Char) {
        val right = stack.pop()
        val left = stack.pop()
        when (operand) {
            ADD -> stack.push(left + right)
            SUBTRACT -> stack.push(left - right)
            MULTIPLY -> stack.push(left * right)
            DIVIDE -> stack.push(left / right)
        }
    }
}
