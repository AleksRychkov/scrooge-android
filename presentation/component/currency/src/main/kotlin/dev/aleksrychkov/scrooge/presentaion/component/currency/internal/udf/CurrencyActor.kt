package dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.actors.FavoriteCurrencyDelegate
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.actors.ObserveCurrenciesDelegate
import kotlinx.coroutines.flow.Flow

internal class CurrencyActor(
    private val observeCurrencies: ObserveCurrenciesDelegate = ObserveCurrenciesDelegate(),
    private val favoriteCurrencies: FavoriteCurrencyDelegate = FavoriteCurrencyDelegate(),
) : Actor<CurrencyCommand, CurrencyEvent> {
    override suspend fun process(command: CurrencyCommand): Flow<CurrencyEvent> {
        return when (command) {
            CurrencyCommand.ObserveCurrencies -> observeCurrencies()
            is CurrencyCommand.AddToFavorite -> favoriteCurrencies.addToFavorite(command)
            is CurrencyCommand.RemoveFromFavorite -> favoriteCurrencies.removeFromFavorite(command)
        }
    }
}
