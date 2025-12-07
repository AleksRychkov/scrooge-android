package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class ReportAmountForPeriodByTypeAndCodeEntity(
    val income: ImmutableList<Value>,
    val expense: ImmutableList<Value>,
    val total: ImmutableList<Value> = persistentListOf(),
) {
    @Serializable
    data class Value(
        val currency: CurrencyEntity,
        val amount: Long,
    )
}
