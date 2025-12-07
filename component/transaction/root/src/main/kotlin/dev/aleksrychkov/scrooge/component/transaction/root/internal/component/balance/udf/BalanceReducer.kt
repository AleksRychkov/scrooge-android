package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import dev.aleksrychkov.scrooge.core.designsystem.composables.DsBalanceData
import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import dev.aleksrychkov.scrooge.core.entity.amountToValue
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList

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
                    )
                }
            }

            is BalanceEvent.Internal.UpdateBalance -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        balance = map(event.result),
                    )
                }
            }
        }
    }

    private fun map(r: ReportAmountForPeriodByTypeAndCodeEntity): DsBalanceData {
        return DsBalanceData(
            income = r.income
                .map { value ->
                    DsBalanceData.Total(
                        currencySymbol = value.currency.currencySymbol,
                        amount = value.amount.amountToValue(),
                    )
                }
                .toImmutableList(),
            expense = r.expense
                .map { value ->
                    DsBalanceData.Total(
                        currencySymbol = value.currency.currencySymbol,
                        amount = value.amount.amountToValue(),
                    )
                }
                .toImmutableList(),
            total = r.total
                .map { value ->
                    DsBalanceData.Total(
                        currencySymbol = value.currency.currencySymbol,
                        amount = value.amount.amountToValue(),
                    )
                }
                .toImmutableList(),
        )
    }
}
