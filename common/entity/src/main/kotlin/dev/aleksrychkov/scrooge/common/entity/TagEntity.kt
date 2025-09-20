package dev.aleksrychkov.scrooge.common.entity

data class TagEntity(
    val id: Long,
    val name: String,
    val colorHex: String? = null,
)
