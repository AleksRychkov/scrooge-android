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
        // not good but will work
        const val DEFAULT_ICON_ID = "QuestionMark"
        const val DEFAULT_COLOR = 0xFF212121.toInt()
        fun from(
            name: String,
            type: TransactionType,
            iconId: String = DEFAULT_ICON_ID,
            color: Int = DEFAULT_COLOR,
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
