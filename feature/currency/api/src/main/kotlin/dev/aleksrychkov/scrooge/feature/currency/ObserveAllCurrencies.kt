package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface ObserveAllCurrencies {
    suspend operator fun invoke(): Result<Flow<ImmutableList<CurrencyEntity>>>
}
