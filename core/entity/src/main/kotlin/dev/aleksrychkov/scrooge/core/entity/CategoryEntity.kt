package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val iconId: String,
    val color: Int,
) {
    companion object {
        fun from(
            name: String,
            type: TransactionType,
            iconId: String = "",
            color: Int = 0xFFFF3B30.toInt(),
        ): CategoryEntity =
            CategoryEntity(
                id = 0L,
                name = name,
                type = type,
                iconId = iconId,
                color = color,
            )
    }
}
