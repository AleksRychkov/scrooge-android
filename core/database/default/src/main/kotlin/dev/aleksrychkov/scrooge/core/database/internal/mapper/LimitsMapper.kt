package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.LimitEntity

internal object LimitsMapper {
    fun toEntity(
        id: Long,
        amount: Long,
        type: Long,
        currencyCode: String,
    ): LimitEntity = LimitEntity(
        id = id,
        amount = amount,
        period = LimitEntity.Period.fromType(type.toInt()),
        currency = CurrencyEntity.fromCurrencyCode(currencyCode)!!,
    )
}
