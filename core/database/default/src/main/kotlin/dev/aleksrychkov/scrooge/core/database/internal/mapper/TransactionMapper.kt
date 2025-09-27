package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.TTransaction
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

internal object TransactionMapper {

    private const val TAGS_SEPARATOR = "|"

    fun toEntity(transaction: TTransaction): TransactionEntity =
        TransactionEntity(
            id = transaction.id,
            amount = transaction.amount,
            timestamp = transaction.timestamp,
            type = TransactionType.from(transaction.type.toInt()),
            category = transaction.category,
            tags = toTags(transaction.tags),
            currencyCode = transaction.currencyCode,
        )

    fun toDatabaseTags(value: Set<String>?): String? {
        if (value == null || value.isEmpty()) return null
        return value.joinToString(TAGS_SEPARATOR) { it.replace(TAGS_SEPARATOR, " ") }
    }

    private fun toTags(value: String?): ImmutableSet<String> {
        if (value == null) return persistentSetOf()
        return value.split(TAGS_SEPARATOR).toImmutableSet()
    }
}
