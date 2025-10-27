package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class TagEntity(
    val id: Long,
    val name: String,
    val colorHex: String? = null,
) {
    companion object {
        fun from(name: String): TagEntity =
            TagEntity(
                id = 0L,
                name = name,
                colorHex = null,
            )
    }
}
