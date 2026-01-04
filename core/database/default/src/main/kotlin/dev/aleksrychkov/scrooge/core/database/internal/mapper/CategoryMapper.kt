package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

internal object CategoryMapper {
    fun toEntity(
        id: Long,
        name: String,
        type: Long,
        iconId: String,
        color: Long,
        orderIndex: Long,
    ): CategoryEntity =
        CategoryEntity(
            id = id,
            type = TransactionType.from(type.toInt()),
            name = name,
            iconId = iconId,
            color = color.toInt(),
            orderIndex = orderIndex.toInt(),
        )
}
