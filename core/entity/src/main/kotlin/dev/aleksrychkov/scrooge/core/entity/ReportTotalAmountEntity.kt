package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class ReportTotalAmountEntity(
    val income: SerializableImmutableList<Value>,
    val expense: SerializableImmutableList<Value>,
    val total: SerializableImmutableList<Value> = persistentListOf(),
) {
    @Serializable
    data class Value(
        val currency: CurrencyEntity,
        val amount: Long,
    )
}
