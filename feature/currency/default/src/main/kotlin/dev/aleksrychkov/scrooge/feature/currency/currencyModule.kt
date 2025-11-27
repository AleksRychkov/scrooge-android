@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.currency

import android.content.Context
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.di.singleton
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultAddToFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultGetCurrenciesUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultGetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultObserveFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultRemoveFromFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultSetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.FavoriteCurrencyRepository
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.LastUsedCurrencyRepository
import dev.aleksrychkov.scrooge.feature.currency.internal.data.source.FavoriteCurrencySource
import dev.aleksrychkov.scrooge.feature.currency.internal.data.source.LastUsedCurrencySource

fun buildCurrencyModule(context: Context): NaiveModule {
    return module {
        factory<FavoriteCurrencySource> {
            FavoriteCurrencySource(
                storeName = "favorite_currency",
                context = context,
            )
        }
        singleton<FavoriteCurrencyRepository> {
            FavoriteCurrencyRepository(source = getLazy())
        }
        factory<GetCurrenciesUseCase> { DefaultGetCurrenciesUseCase() }
        factory<AddToFavoriteCurrencyUseCase> { DefaultAddToFavoriteCurrencyUseCase() }
        factory<RemoveFromFavoriteCurrencyUseCase> { DefaultRemoveFromFavoriteCurrencyUseCase() }
        factory<ObserveFavoriteCurrencyUseCase> { DefaultObserveFavoriteCurrencyUseCase() }
        factory<GetLastUsedCurrencyUseCase> { DefaultGetLastUsedCurrencyUseCase(repository = getLazy()) }
        factory<SetLastUsedCurrencyUseCase> { DefaultSetLastUsedCurrencyUseCase(repository = getLazy()) }
        factory<LastUsedCurrencySource> {
            LastUsedCurrencySource(
                storeName = "last_used_currency",
                context = context,
            )
        }
        singleton<LastUsedCurrencyRepository> {
            LastUsedCurrencyRepository(source = getLazy())
        }
    }
}
