package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.serialization.Serializable

@Serializable
data class TransactionEntity(
    val id: Long,
    val amount: Long,
    val timestamp: Long,
    val type: TransactionType,
    val category: String,
    val tags: ImmutableSet<String>,
    val currency: CurrencyEntity,
)
