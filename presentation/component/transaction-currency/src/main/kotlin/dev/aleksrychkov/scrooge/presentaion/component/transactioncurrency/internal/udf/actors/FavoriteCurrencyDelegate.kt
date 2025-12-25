package dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.currency.AddToFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.RemoveFromFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyCommand
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class FavoriteCurrencyDelegate(
    private val addUseCase: Lazy<AddToFavoriteCurrencyUseCase>,
    private val removeUseCase: Lazy<RemoveFromFavoriteCurrencyUseCase>,
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
