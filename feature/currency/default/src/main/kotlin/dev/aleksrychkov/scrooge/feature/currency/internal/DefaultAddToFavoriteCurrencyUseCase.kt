package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.currency.AddToFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.FavoriteCurrencyRepository

internal class DefaultAddToFavoriteCurrencyUseCase(
    private val repository: Lazy<FavoriteCurrencyRepository> = getLazy(),
) : AddToFavoriteCurrencyUseCase {
    override suspend fun invoke(currency: CurrencyEntity) {
        runSuspendCatching {
            repository.value.addFavoriteCurrency(currency = currency)
        }
    }
}
