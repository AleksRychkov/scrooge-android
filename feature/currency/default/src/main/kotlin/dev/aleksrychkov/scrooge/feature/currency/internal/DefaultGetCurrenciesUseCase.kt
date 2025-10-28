package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.GetCurrenciesUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class DefaultGetCurrenciesUseCase : GetCurrenciesUseCase {
    override suspend fun invoke(): ImmutableList<CurrencyEntity> =
        CurrencyEntity.entries.toImmutableList()
}
