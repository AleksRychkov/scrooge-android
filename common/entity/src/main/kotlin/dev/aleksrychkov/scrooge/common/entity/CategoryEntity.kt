package dev.aleksrychkov.scrooge.common.entity

data class CategoryEntity(
    val id: Long,
    val name: String,
    val type: TransactionType,
    val isUserMade: Boolean,
)
