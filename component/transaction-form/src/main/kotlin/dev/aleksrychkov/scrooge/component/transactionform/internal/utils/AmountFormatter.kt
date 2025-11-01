package dev.aleksrychkov.scrooge.component.transactionform.internal.utils

internal object AmountFormatter {

    private const val AMOUNT_CHUNK_SIZE = 3

    fun sanitizeValue(input: String): String {
        var value = input.trim()
            .replace(",", ".")
            .replace(Regex("\\s+"), "")
            .replace(Regex("[^0-9.,]"), "")

        if (value.isBlank() || value == ".") return ""

        if (value.contains(".")) {
            val indexOfSecondsDelimiter = value.indexOfSecond('.')
            if (indexOfSecondsDelimiter != -1) {
                value = value.take(indexOfSecondsDelimiter)
            }
        }

        val valueSplit = value.split('.')
        var (integerPart, decimalPart) = if (valueSplit.size > 1) {
            valueSplit[0] to valueSplit[1]
        } else {
            value to ""
        }
        integerPart = integerPart.trimStart('0').let {
            it.ifEmpty { "0$it" }
        }
        decimalPart = decimalPart.substring(0, 2.coerceAtMost(decimalPart.length))

        return if (decimalPart.isNotBlank() || value.contains(".")) {
            "$integerPart.$decimalPart"
        } else {
            integerPart
        }
    }

    fun String.formatAmount(delimiter: Char = '.'): String {
        if (this.isBlank()) return this
        val parts = this.split(delimiter)

        val integerPart = parts[0]
            .replace("\\s".toRegex(), "")
            .reversed()
            .chunked(AMOUNT_CHUNK_SIZE)
            .joinToString(" ")
            .reversed()

        return if (parts.size > 1) {
            "$integerPart$delimiter${parts[1]}"
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
