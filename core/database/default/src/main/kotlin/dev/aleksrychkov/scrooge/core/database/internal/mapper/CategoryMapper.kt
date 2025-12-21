package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.Category
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

internal object CategoryMapper {
    fun toEntity(category: Category): CategoryEntity =
        CategoryEntity(
            id = category.id,
            type = TransactionType.from(category.type.toInt()),
            name = category.name,
            iconId = category.iconId,
            color = category.color.toInt(),
        )
}
