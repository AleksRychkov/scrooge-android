package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal

import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER

internal interface AmountInputTransformation {
    companion object {
        operator fun invoke(): AmountInputTransformation {
            return DefaultAmountInputTransformation()
        }
    }

    fun transform(value: String): String
}

private class DefaultAmountInputTransformation : AmountInputTransformation {

    private companion object {
        const val AMOUNT_CHUNK_SIZE = 3
        const val MAX_FRACTION_DIGITS = 2
    }

    @Suppress("CyclomaticComplexMethod", "LoopWithTooManyJumpStatements")
    override fun transform(value: String): String {
        if (value.isEmpty()) return value

        val sb = StringBuilder(value.length + value.length / AMOUNT_CHUNK_SIZE)

        var hasDelimiter = false
        var hasLeadingZero = false
        var fractionDigits = 0

        for (c in value) {
            if (c.isWhitespace()) continue
            if (sb.isEmpty()) {
                when (c) {
                    '.', ',' -> {
                        hasDelimiter = true
                        sb.append('0').append(AMOUNT_DELIMITER)
                    }

                    else -> {
                        hasLeadingZero = c == '0'
                        sb.append(c)
                    }
                }
                continue
            }

            if (c == '.' || c == ',') {
                if (!hasDelimiter) {
                    sb.append(AMOUNT_DELIMITER)
                    hasDelimiter = true
                }
                continue
            }

            if (c == '0' && hasLeadingZero && !hasDelimiter) continue

            if (hasDelimiter) {
                if (fractionDigits >= MAX_FRACTION_DIGITS) continue
                fractionDigits++
                sb.append(c)
                continue
            }

            sb.append(c)
        }

        return normalizeLeadingZeros(groupThousands(sb))
    }

    private fun normalizeLeadingZeros(value: String): String {
        val delimiterIndex = value.indexOf(AMOUNT_DELIMITER)

        val integerPart =
            if (delimiterIndex == -1) {
                value
            } else {
                value.substring(0, delimiterIndex)
            }

        val fractionPart =
            if (delimiterIndex == -1) {
                ""
            } else {
                value.substring(delimiterIndex)
            }

        val normalizedInteger =
            integerPart.trimStart('0').ifEmpty { "0" }

        return normalizedInteger + fractionPart
    }

    private fun groupThousands(sb: StringBuilder): String {
        val delimiterIndex = sb.indexOf(AMOUNT_DELIMITER.toString())
        val integerEnd = if (delimiterIndex == -1) sb.length else delimiterIndex

        val result = StringBuilder(sb.length + sb.length / AMOUNT_CHUNK_SIZE)

        var count = 0
        for (i in integerEnd - 1 downTo 0) {
            if (count == AMOUNT_CHUNK_SIZE) {
                result.append(' ')
                count = 0
            }
            result.append(sb[i])
            count++
        }

        result.reverse()

        if (delimiterIndex != -1) {
            result.append(sb.substring(delimiterIndex))
        }

        return result.toString()
    }
}
