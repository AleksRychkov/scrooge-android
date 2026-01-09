package dev.aleksrychkov.scrooge.presentaion.component.currency.internal.udf

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
                        listOf(CurrencyCommand.ObserveCurrencies)
                    }
                }
            }

            is CurrencyEvent.Internal.Currencies -> {
                state.reduceWith(event) {
                    state {
                        copy(all = event.all, favorite = event.favorite)
                    }
                }
            }

            is CurrencyEvent.External.AddToFavorite -> {
                state.reduceWith(event) {
                    command {
                        listOf(CurrencyCommand.AddToFavorite(currency = event.currency))
                    }
                }
            }

            is CurrencyEvent.External.RemoveFromFavorite -> {
                state.reduceWith(event) {
                    command {
                        listOf(CurrencyCommand.RemoveFromFavorite(currency = event.currency))
                    }
                }
            }
        }
    }
}
