package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import java.util.ArrayDeque

// http://e-maxx.ru/algo/expressions_parsing
internal class InfixCalculator {

    private companion object {
        const val LOCAL_ADD = '+'
        const val LOCAL_SUBTRACT = '-'
        const val LOCAL_MULTIPLY = '*'
        const val LOCAL_DIVIDE = '/'
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NestedBlockDepth", "CyclomaticComplexMethod")
    fun calculate(input: String): Float {
        val tmp = input
            .replace(AMOUNT_DELIMITER, DECIMAL_SEPARATOR)
            .replace(MULTIPLY, LOCAL_MULTIPLY)
            .replace(SUBTRACT, LOCAL_SUBTRACT)
            .replace(DIVIDE, LOCAL_DIVIDE)
            .replace(ADD, LOCAL_ADD)

        val numberStack = ArrayDeque<Float>()
        val operandsStack = ArrayDeque<Int>()
        var maybeUnary = true

        var index = 0
        while (index < tmp.length) {
            val current = tmp[index]
            when {
                current.isDigit() || (maybeUnary && (current == LOCAL_ADD || current == LOCAL_SUBTRACT)) -> {
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
                    maybeUnary = false
                    index = j
                }

                current == OPEN_PARENTHESES -> {
                    operandsStack.push(current.toSigned())
                    maybeUnary = true
                    index++
                }

                current == CLOSE_PARENTHESES -> {
                    while (operandsStack.peek() != OPEN_PARENTHESES.toSigned()) {
                        processOperation(numberStack, operandsStack.pop())
                    }
                    operandsStack.pop()
                    maybeUnary = false
                    index++
                }

                current.isOperand() -> {
                    var currentOperand = current.toSigned()

                    if (maybeUnary && current.isUnaryOperator()) {
                        currentOperand = -currentOperand
                    }

                    while (operandsStack.isNotEmpty()) {
                        val top = operandsStack.peek()
                        val cond = if (currentOperand >= 0) {
                            top.operandPriority() >= currentOperand.operandPriority()
                        } else {
                            top.operandPriority() > currentOperand.operandPriority()
                        }
                        if (!cond) break

                        processOperation(numberStack, operandsStack.pop())
                    }

                    operandsStack.push(currentOperand)
                    maybeUnary = true
                    index++
                }
            }
        }
        while (operandsStack.isNotEmpty()) {
            processOperation(numberStack, operandsStack.pop())
        }
        return numberStack.pop()
    }

    private fun Char.toSigned(): Int = this.code.toByte().toInt()

    private fun Char.isUnaryOperator(): Boolean = this == LOCAL_ADD || this == LOCAL_SUBTRACT

    private fun Int.operandPriority(): Int {
        if (this < 0) {
            return 4
        }
        return when (this.toChar()) {
            LOCAL_ADD, LOCAL_SUBTRACT -> 1
            LOCAL_DIVIDE, LOCAL_MULTIPLY -> 2
            else -> -1
        }
    }

    private fun processOperation(stack: ArrayDeque<Float>, operand: Int) {
        if (operand < 0) {
            val value = stack.pop()
            stack.push(if (-operand == ADD.toSigned()) value else -value)
            return
        }

        val right = stack.pop()
        val left = stack.pop()
        stack.push(
            when (operand) {
                LOCAL_ADD.toSigned() -> left + right
                LOCAL_SUBTRACT.toSigned() -> left - right
                LOCAL_MULTIPLY.toSigned() -> left * right
                LOCAL_DIVIDE.toSigned() -> left / right
                else -> throw IllegalArgumentException("Unknown operator")
            }
        )
    }

    private fun Char.isOperand(): Boolean =
        this == LOCAL_ADD || this == LOCAL_SUBTRACT || this == LOCAL_DIVIDE || this == LOCAL_MULTIPLY
}
