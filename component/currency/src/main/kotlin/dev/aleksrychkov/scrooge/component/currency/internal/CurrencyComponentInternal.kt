package dev.aleksrychkov.scrooge.component.currency.internal

import dev.aleksrychkov.scrooge.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyState
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.coroutines.flow.StateFlow

internal interface CurrencyComponentInternal : CurrencyComponent {
    val state: StateFlow<CurrencyState>

    fun removeFromFavorite(currency: CurrencyEntity)
    fun addToFavorite(currency: CurrencyEntity)
}
