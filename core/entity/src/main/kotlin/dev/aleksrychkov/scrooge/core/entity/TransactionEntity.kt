package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.time.Clock

private const val AMOUNT_CHUNK_SIZE = 3
private const val CENTS = 100L
const val DELIMITER = ','

@Serializable
data class TransactionEntity(
    val id: Long,
    val amount: Long,
    val timestamp: Long,
    val type: TransactionType,
    val category: String,
    val tags: ImmutableSet<String>,
    val currency: CurrencyEntity,
) {
    companion object {
        val DUMMY: TransactionEntity = TransactionEntity(
            id = 0,
            amount = 125,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            type = TransactionType.Income,
            category = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua",
            tags = persistentSetOf(
                "tag1",
                "tag2",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                    " incididunt ut labore et dolore magna aliqua"
            ),
            currency = CurrencyEntity.RUB,
        )
    }
}

// todo: tests?
fun Long.amountToValue(): String {
    val sign = if (this < 0) "-" else ""
    val absValue = abs(this)
    val major = absValue / CENTS
    val minor = absValue % CENTS
    return "$sign$major${DELIMITER}${minor.toString().padStart(2, '0')}".formatAmount()
}

fun String.formatAmount(delimiter: Char = DELIMITER): String {
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
