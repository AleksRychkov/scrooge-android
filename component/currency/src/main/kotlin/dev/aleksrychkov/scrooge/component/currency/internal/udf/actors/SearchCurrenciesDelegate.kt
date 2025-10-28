package dev.aleksrychkov.scrooge.component.currency.internal.udf.actors

import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyCommand
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyEvent
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SearchCurrenciesDelegate {
    operator fun invoke(cmd: CurrencyCommand.Search): Flow<CurrencyEvent> {
        val filtered = when {
            cmd.query.isBlank() -> persistentListOf()
            else ->
                cmd.currencies
                    .filter { it.searchPredicate(cmd.query) }
                    .toImmutableList()
        }
        return flowOf(CurrencyEvent.Internal.Filtered(currencies = filtered))
    }

    private fun CurrencyEntity.searchPredicate(query: String): Boolean =
        currencyName.lowercase().contains(query.lowercase()) ||
            currencyCode.lowercase().contains(query.lowercase()) ||
            currencyNumCode.lowercase().contains(query.lowercase())
}
