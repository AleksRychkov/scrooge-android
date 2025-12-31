package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

internal object TransactionMapper {

    private const val TAGS_SEPARATOR = "|"

    @Suppress("LongParameterList")
    fun transactionEntityMapper(
        id: Long,
        amount: Long,
        datestamp: Long,
        category: String,
        tags: String?,
        type: Long,
        currencyCode: String,
        categoryId: Long?,
        categoryName: String?,
        categoryIconId: String?,
        categoryType: Long?,
        categoryColor: Long?,
    ): TransactionEntity {
        return TransactionEntity(
            id = id,
            amount = amount,
            datestamp = Datestamp(datestamp),
            type = TransactionType.from(type.toInt()),
            category = if (categoryId != null) {
                CategoryEntity(
                    id = categoryId,
                    name = categoryName ?: category,
                    type = TransactionType.from(categoryType!!.toInt()),
                    iconId = categoryIconId ?: CategoryEntity.DEFAULT_ICON_ID,
                    color = categoryColor?.toInt() ?: CategoryEntity.DEFAULT_COLOR,
                )
            } else {
                CategoryEntity.from(
                    name = category,
                    type = TransactionType.from(type.toInt()),
                )
            },
            tags = tags?.split(TAGS_SEPARATOR)?.toImmutableSet() ?: persistentSetOf(),
            currency = CurrencyEntity.fromCurrencyCode(currencyCode)!!,
        )
    }

    fun toDatabaseTags(value: Set<String>?): String? {
        if (value == null || value.isEmpty()) return null
        return value.sorted().joinToString(TAGS_SEPARATOR) { it.replace(TAGS_SEPARATOR, " ") }
    }

    fun toTags(value: String?): ImmutableSet<String> {
        if (value == null) return persistentSetOf()
        return value.split(TAGS_SEPARATOR).toImmutableSet()
    }
}
