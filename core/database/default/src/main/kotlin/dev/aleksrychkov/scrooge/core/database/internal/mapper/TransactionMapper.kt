package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.BuildConfig
import dev.aleksrychkov.scrooge.core.database.TagDao.Companion.TAG_DELIMITER
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

internal object TransactionMapper {

    @Suppress("LongParameterList", "TooGenericExceptionCaught")
    fun transactionEntityMapper(
        id: Long,
        amount: Long,
        datestamp: Long,
        category: String,
        type: Long,
        currencyCode: String,
        categoryId: Long?,
        categoryName: String?,
        categoryIconId: String?,
        categoryType: Long?,
        categoryColor: Long?,
        tags: String?,
        tagIds: String?,
    ): TransactionEntity {
        val tagEntities = if (tags != null && tagIds != null) {
            try {
                val tagNames = tags.split(TAG_DELIMITER)
                tagIds.split(TAG_DELIMITER)
                    .mapIndexed { index, id ->
                        TagEntity(
                            id = id.toLong(),
                            name = tagNames[index],
                        )
                    }
                    .toImmutableSet()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    throw e
                } else {
                    persistentSetOf()
                }
            }
        } else {
            persistentSetOf()
        }
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
            currency = CurrencyEntity.fromCurrencyCode(currencyCode)!!,
            tags = tagEntities,
        )
    }
}
