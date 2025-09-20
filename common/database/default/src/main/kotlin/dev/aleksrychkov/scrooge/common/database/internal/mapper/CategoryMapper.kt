package dev.aleksrychkov.scrooge.common.database.internal.mapper

import dev.aleksrychkov.scrooge.common.database.Category
import dev.aleksrychkov.scrooge.common.entity.CategoryEntity
import dev.aleksrychkov.scrooge.common.entity.TransactionType

internal object CategoryMapper {
    fun toEntity(category: Category): CategoryEntity =
        CategoryEntity(
            id = category.id,
            type = TransactionType.from(category.type.toInt()),
            name = category.name,
            isUserMade = category.isUserMade == 1L,
        )
}
