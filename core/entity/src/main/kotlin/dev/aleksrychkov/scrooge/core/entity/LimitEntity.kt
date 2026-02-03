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
        Monthly(type = 2);

        companion object {
            fun fromType(type: Int): Period =
                when (type) {
                    0 -> Daily
                    1 -> Weekly
                    2 -> Monthly
                    else -> error("Unknown period type: $type")
                }
        }
    }
}
