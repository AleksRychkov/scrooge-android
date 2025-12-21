package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class ReportByCategoryEntity(
    val income: ImmutableList<ByCurrency> = persistentListOf(),
    val expense: ImmutableList<ByCurrency> = persistentListOf(),
) {
    @Serializable
    data class ByCurrency(
        val currency: CurrencyEntity,
        val data: ImmutableList<Value>,
    ) {
        @Serializable
        data class Value(
            val category: CategoryEntity,
            val amount: Long,
        )
    }
}
