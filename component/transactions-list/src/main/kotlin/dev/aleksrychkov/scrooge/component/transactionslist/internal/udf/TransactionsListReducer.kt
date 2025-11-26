package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

import dev.aleksrychkov.scrooge.component.transactionslist.internal.udf.TransactionsListCommand.LoadTransactions
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.persistentListOf

internal class TransactionsListReducer :
    Reducer<TransactionsListState, TransactionsListEvent, TransactionsListCommand, Unit> {

    private val mapper: TransactionsListMapper by lazy {
        TransactionsListMapper()
    }

    override fun reduce(
        event: TransactionsListEvent,
        state: TransactionsListState
    ): ReducerResult<TransactionsListState, TransactionsListCommand, Unit> {
        return when (event) {
            is TransactionsListEvent.External.Initial -> state.reduceWith(event) {
                command {
                    listOf(LoadTransactions(period = event.period))
                }
                state {
                    copy(isLoading = true)
                }
            }

            TransactionsListEvent.Internal.FailedToLoadTransactions -> state.reduceWith(event) {
                state {
                    copy(isLoading = false, transactions = persistentListOf())
                }
            }

            is TransactionsListEvent.Internal.SuccessToLoadTransactions -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        transactions = mapper.transactionsToDayTransactions(event.transactions),
                    )
                }
            }
        }
    }
}
