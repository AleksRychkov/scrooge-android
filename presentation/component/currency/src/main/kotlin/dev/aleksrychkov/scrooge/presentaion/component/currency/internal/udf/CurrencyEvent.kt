package dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.entity.FavoriteCurrencyEntity
import kotlinx.collections.immutable.ImmutableList

internal sealed interface CurrencyEvent {
    sealed interface External : CurrencyEvent {
        data object Init : External
        data class AddToFavorite(val currency: CurrencyEntity) : External
        data class RemoveFromFavorite(val currency: CurrencyEntity) : External
    }

    sealed interface Internal : CurrencyEvent {
        data class Currencies(
            val all: ImmutableList<FavoriteCurrencyEntity>,
            val favorite: ImmutableList<FavoriteCurrencyEntity>,
        ) : Internal
    }
}
