package dev.aleksrychkov.scrooge.common.entity

import kotlinx.collections.immutable.ImmutableSet

data class TransactionEntity(
    val id: Long,
    val amount: Long,
    val timestamp: Long,
    val type: TransactionType,
    val category: String,
    val tags: ImmutableSet<String>,
    val currencyCode: CurrencyCode,
)
