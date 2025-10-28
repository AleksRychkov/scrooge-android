package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

fun interface AddToFavoriteCurrencyUseCase {
    suspend operator fun invoke(currency: CurrencyEntity)
}
