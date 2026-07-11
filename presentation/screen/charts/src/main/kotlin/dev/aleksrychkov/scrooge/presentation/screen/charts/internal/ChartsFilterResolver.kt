package dev.aleksrychkov.scrooge.presentation.screen.charts.internal

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.feature.category.GetRandomCategoryResult
import dev.aleksrychkov.scrooge.feature.category.GetRandomCategoryUseCase
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCategoryResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCategoryUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyUseCase

internal class ChartsFilterResolver(
    private val mostUsedCurrencyUseCase: Lazy<GetMostUsedCurrencyUseCase> = getLazy(),
    private val lastUsedCurrencyUseCase: Lazy<GetLastUsedCurrencyUseCase> = getLazy(),
    private val mostUsedCategoryUseCase: Lazy<GetMostUsedCategoryUseCase> = getLazy(),
    private val randomCategoryUseCase: Lazy<GetRandomCategoryUseCase> = getLazy(),
) {
    suspend operator fun invoke(filter: FilterEntity): FilterEntity {
        val withCurrency = filter.currency?.let { filter }
            ?: filter.copy(currency = resolveCurrency(filter))
        return withCurrency.category?.let { withCurrency }
            ?: withCurrency.copy(category = resolveCategory(withCurrency))
    }

    private suspend fun resolveCurrency(filter: FilterEntity): CurrencyEntity {
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

    private suspend fun resolveCategory(filter: FilterEntity) =
        when (val mostUsed = mostUsedCategoryUseCase.value(filter.copy(category = null))) {
            is GetMostUsedCategoryResult.Success -> mostUsed.category
            GetMostUsedCategoryResult.Empty -> when (
                val random = randomCategoryUseCase.value(filter.transactionType)
            ) {
                is GetRandomCategoryResult.Success -> random.category
                GetRandomCategoryResult.Empty -> null
            }
        }
}
