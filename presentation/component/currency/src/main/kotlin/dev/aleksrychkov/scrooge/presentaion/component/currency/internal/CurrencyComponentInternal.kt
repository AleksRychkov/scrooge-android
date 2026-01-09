package dev.aleksrychkov.scrooge.presentaion.component.currency.internal

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf.CurrencyState
import kotlinx.coroutines.flow.StateFlow

internal interface CurrencyComponentInternal : CurrencyComponent {
    val state: StateFlow<CurrencyState>

    fun removeFromFavorite(currency: CurrencyEntity)
    fun addToFavorite(currency: CurrencyEntity)
}
