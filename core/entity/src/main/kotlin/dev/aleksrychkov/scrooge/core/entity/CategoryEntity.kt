package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val iconId: String,
) {
    companion object {
        fun from(
            name: String,
            type: TransactionType,
            iconId: String = "",
        ): CategoryEntity =
            CategoryEntity(
                id = 0L,
                name = name,
                type = type,
                iconId = iconId,
            )
    }
}
