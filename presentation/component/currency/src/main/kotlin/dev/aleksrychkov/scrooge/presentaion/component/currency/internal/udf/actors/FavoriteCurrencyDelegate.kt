package dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.currency.AddToFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.RemoveFromFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.CurrencyCommand
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.CurrencyEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class FavoriteCurrencyDelegate(
    private val addUseCase: Lazy<AddToFavoriteCurrencyUseCase> = getLazy(),
    private val removeUseCase: Lazy<RemoveFromFavoriteCurrencyUseCase> = getLazy(),
) {

    suspend fun addToFavorite(cmd: CurrencyCommand.AddToFavorite): Flow<CurrencyEvent> {
        addUseCase.value.invoke(currency = cmd.currency)
        return emptyFlow()
    }

    suspend fun removeFromFavorite(cmd: CurrencyCommand.RemoveFromFavorite): Flow<CurrencyEvent> {
        removeUseCase.value.invoke(currency = cmd.currency)
        return emptyFlow()
    }
}
