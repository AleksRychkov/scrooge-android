package dev.aleksrychkov.scrooge.component.transactionform.internal.utils

import dev.aleksrychkov.scrooge.core.entity.DELIMITER

internal object AmountFormatter {

    fun sanitizeValue(input: String): String {
        var value = input.trim()
            .replace('.', DELIMITER)
            .replace(Regex("\\s+"), "")
            .replace(Regex("[^0-9.,]"), "")

        if (value.isBlank() || value == DELIMITER.toString()) return ""

        if (value.contains(DELIMITER)) {
            val indexOfSecondsDelimiter = value.indexOfSecond(DELIMITER)
            if (indexOfSecondsDelimiter != -1) {
                value = value.take(indexOfSecondsDelimiter)
            }
        }

        val valueSplit = value.split(DELIMITER)
        var (integerPart, decimalPart) = if (valueSplit.size > 1) {
            valueSplit[0] to valueSplit[1]
        } else {
            value to ""
        }
        integerPart = integerPart.trimStart('0').let {
            it.ifEmpty { "0$it" }
        }
        decimalPart = decimalPart.substring(0, 2.coerceAtMost(decimalPart.length))

        return if (decimalPart.isNotBlank() || value.contains(DELIMITER)) {
            "$integerPart$DELIMITER$decimalPart"
        } else {
            integerPart
        }
    }

    private fun CharSequence.indexOfSecond(string: Char): Int {
        var isFirstMatchFound = false
        this.forEachIndexed { index, c ->
            if (c != string) return@forEachIndexed
            if (!isFirstMatchFound) {
                isFirstMatchFound = true
            } else {
                return index
            }
        }
        return -1
    }
}
