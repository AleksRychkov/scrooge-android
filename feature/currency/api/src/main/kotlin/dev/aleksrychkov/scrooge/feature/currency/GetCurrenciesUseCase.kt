package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList

fun interface GetCurrenciesUseCase {
    suspend operator fun invoke(): ImmutableList<CurrencyEntity>
}
