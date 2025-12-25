package dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyState
import kotlinx.coroutines.flow.StateFlow

internal interface CurrencyComponentInternal : CurrencyComponent {
    val state: StateFlow<CurrencyState>

    fun removeFromFavorite(currency: CurrencyEntity)
    fun addToFavorite(currency: CurrencyEntity)
}
