package dev.aleksrychkov.scrooge.component.currency.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

sealed interface CurrencyCommand {
    data object GetCurrencies : CurrencyCommand
    data class Search(
        val query: String,
        val currencies: List<CurrencyEntity>,
    ) : CurrencyCommand
}
