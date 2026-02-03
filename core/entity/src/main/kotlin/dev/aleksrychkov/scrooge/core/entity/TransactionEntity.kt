package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable
import kotlin.math.abs

private const val AMOUNT_CHUNK_SIZE = 3
private const val CENTS = 100L
const val AMOUNT_DELIMITER = ','
const val AMOUNT_DELIMITER_STRING = ","

@Serializable
data class TransactionEntity(
    val id: Long,
    val amount: Long,
    val datestamp: Datestamp,
    val type: TransactionType,
    val category: CategoryEntity,
    val tags: SerializableImmutableSet<TagEntity> = persistentSetOf(),
    val currency: CurrencyEntity,
    val comment: String = "",
) {
    companion object {
        val DUMMY: TransactionEntity = TransactionEntity(
            id = 0,
            amount = 125,
            datestamp = Datestamp.now(),
            type = TransactionType.Income,
            category = CategoryEntity.from("Category", TransactionType.Income),
            tags = persistentSetOf(TagEntity(0L, "tag1")),
            currency = CurrencyEntity.RUB,
        )
    }
}

fun Long.amountToString(): String {
    val major = this / CENTS
    val minor = this % CENTS
    return "$major${AMOUNT_DELIMITER}${minor.toString().padStart(2, '0')}"
}

fun Long.amountToStringFormatted(
    forceSign: String? = null,
): String {
    val sign = forceSign ?: if (this < 0) "-" else "+"
    val absValue = abs(this)
    val major = absValue / CENTS
    val minor = absValue % CENTS

    val majorFormatted = major.toString()
        .replace("\\s".toRegex(), "")
        .reversed()
        .chunked(AMOUNT_CHUNK_SIZE)
        .joinToString(" ")
        .reversed()

    return "$sign$majorFormatted${AMOUNT_DELIMITER}${minor.toString().padStart(2, '0')}"
}

fun String.toCents(): Long {
    val amountRegex = Regex("[^0-9,]")
    val clean = this.replace(amountRegex, "")
    val parts = clean.split(",")

    val euros = parts.getOrNull(0).orEmpty()
    val cents = parts.getOrNull(1).orEmpty()

    val centsPadded = when (cents.length) {
        0 -> "00"
        1 -> cents + "0"
        else -> cents.take(2)
    }
    return (euros.ifEmpty { "0" } + centsPadded).toLong()
}
