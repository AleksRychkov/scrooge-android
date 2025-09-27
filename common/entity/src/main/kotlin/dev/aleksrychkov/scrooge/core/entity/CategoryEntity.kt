package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val isUserMade: Boolean,
)
