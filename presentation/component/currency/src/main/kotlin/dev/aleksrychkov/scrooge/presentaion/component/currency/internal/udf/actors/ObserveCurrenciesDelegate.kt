package dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.GetCurrenciesUseCase
import dev.aleksrychkov.scrooge.feature.currency.ObserveFavoriteCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.ObserveFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.entity.FavoriteCurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.CurrencyEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class ObserveCurrenciesDelegate(
    private val getAllUseCase: Lazy<GetCurrenciesUseCase> = getLazy(),
    private val observeFavoriteUseCase: Lazy<ObserveFavoriteCurrencyUseCase> = getLazy(),
) {

    @Volatile
    private var allCurrencies: ImmutableList<CurrencyEntity>? = null

    suspend operator fun invoke(): Flow<CurrencyEvent> {
        val result = observeFavoriteUseCase
            .value
            .invoke()

        if (result is ObserveFavoriteCurrencyResult.Success) {
            return result
                .favorite
                .map { favorite ->
                    var all = allCurrencies
                    if (all == null) {
                        all = getAllUseCase.value.invoke()
                        allCurrencies = all
                    }

                    CurrencyEvent.Internal.Currencies(
                        all = all.map {
                            FavoriteCurrencyEntity(
                                currency = it,
                                isFavorite = favorite.contains(it),
                            )
                        }.toPersistentList(),
                        favorite = favorite.map {
                            FavoriteCurrencyEntity(
                                currency = it,
                                isFavorite = true
                            )
                        }.toPersistentList(),
                    )
                }
        } else {
            var all = allCurrencies
            if (all == null) {
                all = getAllUseCase.value.invoke()
                allCurrencies = all
            }
            return flowOf(
                CurrencyEvent.Internal.Currencies(
                    all = all.map { FavoriteCurrencyEntity(currency = it) }.toPersistentList(),
                    favorite = persistentListOf()
                )
            )
        }
    }
}
