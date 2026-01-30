package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class LimitEntity(
    val id: Long,
    val currency: CurrencyEntity,
    val amount: Long,
    val period: Period,
) {
    enum class Period(val type: Int) {
        Daily(type = 0),
        Weekly(type = 1),
        Monthly(type = 2)
    }
}
