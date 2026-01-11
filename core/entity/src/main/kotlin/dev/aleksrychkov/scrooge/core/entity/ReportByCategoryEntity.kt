package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class ReportByCategoryEntity(
    val income: SerializableImmutableList<ByCurrency> = persistentListOf(),
    val expense: SerializableImmutableList<ByCurrency> = persistentListOf(),
) {
    @Serializable
    data class ByCurrency(
        val currency: CurrencyEntity,
        val data: SerializableImmutableList<Value>,
    ) {
        @Serializable
        data class Value(
            val category: CategoryEntity,
            val amount: Long,
        )
    }
}
