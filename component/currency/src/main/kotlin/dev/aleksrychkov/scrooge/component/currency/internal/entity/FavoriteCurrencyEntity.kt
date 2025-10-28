package dev.aleksrychkov.scrooge.component.currency.internal.entity

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

@Immutable
internal data class FavoriteCurrencyEntity(
    val currency: CurrencyEntity,
    val isFavorite: Boolean = false,
)
