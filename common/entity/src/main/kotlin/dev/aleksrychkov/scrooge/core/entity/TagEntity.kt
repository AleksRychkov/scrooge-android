package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class TagEntity(
    val id: Long,
    val name: String,
    val colorHex: String? = null,
)
