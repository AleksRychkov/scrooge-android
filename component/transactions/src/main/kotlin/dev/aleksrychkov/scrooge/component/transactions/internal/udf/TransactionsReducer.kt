package dev.aleksrychkov.scrooge.component.transactions.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class TransactionsReducer :
    Reducer<TransactionsState, TransactionsEvent, TransactionsCommand, TransactionsEffect> {
    override fun reduce(
        event: TransactionsEvent,
        state: TransactionsState
    ): ReducerResult<TransactionsState, TransactionsCommand, TransactionsEffect> {
        return when (event) {
            TransactionsEvent.External.Init -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(TransactionsCommand.Loading)
                }
            }

            TransactionsEvent.Internal.Finish -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
            }
        }
    }
}
