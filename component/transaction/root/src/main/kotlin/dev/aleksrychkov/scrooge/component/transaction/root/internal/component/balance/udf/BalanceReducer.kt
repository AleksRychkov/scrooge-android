package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.persistentListOf

internal class BalanceReducer : Reducer<BalanceState, BalanceEvent, BalanceCommand, Unit> {
    override fun reduce(
        event: BalanceEvent,
        state: BalanceState
    ): ReducerResult<BalanceState, BalanceCommand, Unit> {
        return when (event) {
            is BalanceEvent.External.SetPeriod -> state.reduceWith(event) {
                command {
                    listOf(BalanceCommand.UpdateBalance(instant = event.instant))
                }
                state {
                    copy(isLoading = true, period = event.instant)
                }
            }

            BalanceEvent.Internal.FailedToUpdateBalance -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        income = persistentListOf(),
                        expense = persistentListOf(),
                        total = persistentListOf(),
                    )
                }
            }

            is BalanceEvent.Internal.UpdateBalance -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        income = event.income,
                        expense = event.expense,
                        total = event.total,
                    )
                }
            }
        }
    }
}
