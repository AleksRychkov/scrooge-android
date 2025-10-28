package dev.aleksrychkov.scrooge.component.currency.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList

sealed interface CurrencyEvent {
    sealed interface External : CurrencyEvent {
        data object Init : External
        data class Search(val query: String) : External
    }

    sealed interface Internal : CurrencyEvent {
        data class Currencies(val currencies: ImmutableList<CurrencyEntity>) : Internal
        data class Filtered(val currencies: ImmutableList<CurrencyEntity>) : Internal
    }
}
