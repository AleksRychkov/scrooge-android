package dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

sealed interface CurrencyCommand {
    data object ObserveCurrencies : CurrencyCommand
    data class AddToFavorite(val currency: CurrencyEntity) : CurrencyCommand
    data class RemoveFromFavorite(val currency: CurrencyEntity) : CurrencyCommand
}
