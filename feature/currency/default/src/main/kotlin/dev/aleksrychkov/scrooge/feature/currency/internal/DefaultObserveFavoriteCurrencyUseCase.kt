package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.currency.ObserveFavoriteCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.ObserveFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.FavoriteCurrencyRepository

internal class DefaultObserveFavoriteCurrencyUseCase(
    private val repository: Lazy<FavoriteCurrencyRepository> = getLazy()
) : ObserveFavoriteCurrencyUseCase {
    override suspend fun invoke(): ObserveFavoriteCurrencyResult {
        return runSuspendCatching {
            val flow = repository.value.observeFavoriteCurrencies()
            ObserveFavoriteCurrencyResult.Success(flow)
        }.getOrDefault(ObserveFavoriteCurrencyResult.Failure)
    }
}
