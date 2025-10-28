package dev.aleksrychkov.scrooge.component.currency.internal.udf

import dev.aleksrychkov.scrooge.component.currency.internal.udf.actors.FavoriteCurrencyDelegate
import dev.aleksrychkov.scrooge.component.currency.internal.udf.actors.ObserveCurrenciesDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow

internal class CurrencyActor(
    private val observeCurrencies: ObserveCurrenciesDelegate,
    private val favoriteCurrencies: FavoriteCurrencyDelegate,
) : Actor<CurrencyCommand, CurrencyEvent> {

    companion object {
        operator fun invoke(): CurrencyActor {
            return CurrencyActor(
                observeCurrencies = ObserveCurrenciesDelegate(
                    getAllUseCase = getLazy(),
                    observeFavoriteUseCase = getLazy(),
                ),
                favoriteCurrencies = FavoriteCurrencyDelegate(
                    addUseCase = getLazy(),
                    removeUseCase = getLazy(),
                ),
            )
        }
    }

    override suspend fun process(command: CurrencyCommand): Flow<CurrencyEvent> {
        return when (command) {
            CurrencyCommand.ObserveCurrencies -> observeCurrencies()
            is CurrencyCommand.AddToFavorite -> favoriteCurrencies.addToFavorite(command)
            is CurrencyCommand.RemoveFromFavorite -> favoriteCurrencies.removeFromFavorite(command)
        }
    }
}
