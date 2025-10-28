package dev.aleksrychkov.scrooge.component.currency.internal.udf

import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyCommand.Search
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class CurrencyReducer :
    Reducer<CurrencyState, CurrencyEvent, CurrencyCommand, CurrencyEffect> {
    override fun reduce(
        event: CurrencyEvent,
        state: CurrencyState
    ): ReducerResult<CurrencyState, CurrencyCommand, CurrencyEffect> {
        return when (event) {
            CurrencyEvent.External.Init -> {
                state.reduceWith(event) {
                    command {
                        listOf(CurrencyCommand.GetCurrencies)
                    }
                }
            }

            is CurrencyEvent.External.Search -> {
                state.reduceWith(event) {
                    command {
                        listOf(
                            Search(
                                query = event.query,
                                currencies = currencies.toList(),
                            )
                        )
                    }
                    state {
                        copy(searchQuery = event.query)
                    }
                }
            }

            is CurrencyEvent.Internal.Currencies -> {
                state.reduceWith(event) {
                    if (state.searchQuery.isNotBlank()) {
                        command {
                            listOf(
                                Search(
                                    query = state.searchQuery,
                                    currencies = event.currencies.toList(),
                                )
                            )
                        }
                    }
                    state {
                        copy(currencies = event.currencies)
                    }
                }
            }

            is CurrencyEvent.Internal.Filtered -> {
                state.reduceWith(event) {
                    state {
                        copy(filtered = event.currencies)
                    }
                }
            }
        }
    }
}
