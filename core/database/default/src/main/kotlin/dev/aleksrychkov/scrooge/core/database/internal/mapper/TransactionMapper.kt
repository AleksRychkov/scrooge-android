package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.SelectById
import dev.aleksrychkov.scrooge.core.database.SelectFromTo
import dev.aleksrychkov.scrooge.core.database.SelectFromToAndTags
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

    fun toEntity(value: SelectFromTo): TransactionEntity =
        TransactionEntity(
            id = value.id,
            amount = value.amount,
            datestamp = Datestamp(value.datestamp),
            type = TransactionType.from(value.type.toInt()),
            category = toCategoryEntity(value),
            tags = toTags(value.tags),
            currency = CurrencyEntity.fromCurrencyCode(value.currencyCode)!!,
        )

    fun toEntity(value: SelectFromToAndTags): TransactionEntity =
        TransactionEntity(
            id = value.id,
            amount = value.amount,
            datestamp = Datestamp(value.datestamp),
            type = TransactionType.from(value.type.toInt()),
            category = toCategoryEntity(value),
            tags = toTags(value.tags),
            currency = CurrencyEntity.fromCurrencyCode(value.currencyCode)!!,
        )

    fun toEntity(value: SelectById): TransactionEntity =
        TransactionEntity(
            id = value.id,
            amount = value.amount,
            datestamp = Datestamp(value.datestamp),
            type = TransactionType.from(value.type.toInt()),
            category = toCategoryEntity(value),
            tags = toTags(value.tags),
            currency = CurrencyEntity.fromCurrencyCode(value.currencyCode)!!,
        )

    fun toDatabaseTags(value: Set<String>?): String? {
        if (value == null || value.isEmpty()) return null
        return value.sorted().joinToString(TAGS_SEPARATOR) { it.replace(TAGS_SEPARATOR, " ") }
    }

    private fun toCategoryEntity(value: SelectFromTo): CategoryEntity {
        return if (value.categoryId != null) {
            CategoryEntity(
                id = value.categoryId,
                name = value.categoryName ?: value.category,
                type = TransactionType.from(value.categoryType!!.toInt()),
                iconId = value.categoryIconId ?: CategoryEntity.DEFAULT_ICON_ID,
                color = value.categoryColor?.toInt() ?: CategoryEntity.DEFAULT_COLOR,
            )
        } else {
            CategoryEntity.from(
                name = value.category,
                type = TransactionType.from(value.type.toInt()),
            )
        }
    }

    private fun toCategoryEntity(value: SelectById): CategoryEntity {
        return if (value.categoryId != null) {
            CategoryEntity(
                id = value.categoryId,
                name = value.categoryName ?: value.category,
                type = TransactionType.from(value.categoryType!!.toInt()),
                iconId = value.categoryIconId ?: CategoryEntity.DEFAULT_ICON_ID,
                color = value.categoryColor?.toInt() ?: CategoryEntity.DEFAULT_COLOR,
            )
        } else {
            CategoryEntity.from(
                name = value.category,
                type = TransactionType.from(value.type.toInt()),
            )
        }
    }

    private fun toCategoryEntity(value: SelectFromToAndTags): CategoryEntity {
        return if (value.categoryId != null) {
            CategoryEntity(
                id = value.categoryId,
                name = value.categoryName ?: value.category,
                type = TransactionType.from(value.categoryType!!.toInt()),
                iconId = value.categoryIconId ?: CategoryEntity.DEFAULT_ICON_ID,
                color = value.categoryColor?.toInt() ?: CategoryEntity.DEFAULT_COLOR,
            )
        } else {
            CategoryEntity.from(
                name = value.category,
                type = TransactionType.from(value.type.toInt()),
            )
        }
    }

    fun toTags(value: String?): ImmutableSet<String> {
        if (value == null) return persistentSetOf()
        return value.split(TAGS_SEPARATOR).toImmutableSet()
    }
}
