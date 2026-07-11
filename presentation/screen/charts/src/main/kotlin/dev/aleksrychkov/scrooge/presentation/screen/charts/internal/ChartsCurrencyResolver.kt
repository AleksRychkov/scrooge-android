package dev.aleksrychkov.scrooge.presentation.screen.charts.internal

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyUseCase

internal class ChartsCurrencyResolver(
    private val mostUsedCurrencyUseCase: Lazy<GetMostUsedCurrencyUseCase> = getLazy(),
    private val lastUsedCurrencyUseCase: Lazy<GetLastUsedCurrencyUseCase> = getLazy(),
) {
    suspend operator fun invoke(filter: FilterEntity): CurrencyEntity {
        return when (val mostUsed = mostUsedCurrencyUseCase.value(filter.copy(currency = null))) {
            is GetMostUsedCurrencyResult.Success -> mostUsed.currency
            GetMostUsedCurrencyResult.Empty -> lastUsedCurrency()
        }
    }

    private suspend fun lastUsedCurrency(): CurrencyEntity {
        return when (val lastUsed = lastUsedCurrencyUseCase.value()) {
            is GetLastUsedCurrencyResult.Success -> lastUsed.currency
            GetLastUsedCurrencyResult.Empty -> CurrencyEntity.RUB
        }
    }
}
