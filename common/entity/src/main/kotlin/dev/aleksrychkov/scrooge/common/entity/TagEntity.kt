package dev.aleksrychkov.scrooge.common.entity

import kotlinx.serialization.Serializable

@Serializable
data class TagEntity(
    val id: Long,
    val name: String,
    val colorHex: String? = null,
)
