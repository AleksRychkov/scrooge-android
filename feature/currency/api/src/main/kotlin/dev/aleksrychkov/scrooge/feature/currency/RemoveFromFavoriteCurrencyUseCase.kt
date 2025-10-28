package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

fun interface RemoveFromFavoriteCurrencyUseCase {
    suspend operator fun invoke(currency: CurrencyEntity)
}
