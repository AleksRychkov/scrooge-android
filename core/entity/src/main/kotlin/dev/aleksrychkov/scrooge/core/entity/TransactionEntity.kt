package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.serialization.Serializable
import kotlin.math.abs

private const val AMOUNT_CHUNK_SIZE = 3
private const val CENTS = 100L

@Serializable
data class TransactionEntity(
    val id: Long,
    val amount: Long,
    val timestamp: Long,
    val type: TransactionType,
    val category: String,
    val tags: ImmutableSet<String>,
    val currency: CurrencyEntity,
)

// todo: tests?
fun Long.amountToValue(): String {
    val sign = if (this < 0) "-" else ""
    val absValue = abs(this)
    val major = absValue / CENTS
    val minor = absValue % CENTS
    return "$sign$major.${minor.toString().padStart(2, '0')}".formatAmount()
}

private fun String.formatAmount(delimiter: Char = '.'): String {
    if (this.isBlank()) return this
    val sign = if (this.contains("-")) "-" else ""

    val parts = this.replace("-", "").split(delimiter)

    val integerPart = parts[0]
        .replace("\\s".toRegex(), "")
        .reversed()
        .chunked(AMOUNT_CHUNK_SIZE)
        .joinToString(" ")
        .reversed()

    return if (parts.size > 1) {
        "$sign $integerPart$delimiter${parts[1]}"
    } else {
        "$sign $integerPart"
    }
}
