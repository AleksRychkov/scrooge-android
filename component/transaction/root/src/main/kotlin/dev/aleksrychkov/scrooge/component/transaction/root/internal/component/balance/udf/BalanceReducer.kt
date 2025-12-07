package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import dev.aleksrychkov.scrooge.core.designsystem.composables.DsBalanceData
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToValue
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.ImmutableList
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
                        balanceData = DsBalanceData()
                    )
                }
            }

            is BalanceEvent.Internal.UpdateBalance -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        balanceData = mapTransactionsToBalanceData(event.transactions),
                    )
                }
            }
        }
    }

    private fun mapTransactionsToBalanceData(
        transactions: ImmutableList<TransactionEntity>
    ): DsBalanceData {
        val groupedByCurrency = transactions
            .sortedBy { it.currency }
            .groupBy { it.currency }

        val incomeItems = groupedByCurrency.map { (currency, txs) ->
            val incomeSum = txs
                .filter { it.type == TransactionType.Income }
                .sumOf { it.amount }
            DsBalanceData.Total(
                currencySymbol = currency.currencySymbol,
                value = incomeSum.amountToValue()
            )
        }.toImmutableList()

        val expenseItems = groupedByCurrency.map { (currency, txs) ->
            val expenseSum = txs
                .filter { it.type == TransactionType.Expense }
                .sumOf { it.amount }
            DsBalanceData.Total(
                currencySymbol = currency.currencySymbol,
                value = expenseSum.amountToValue()
            )
        }.toImmutableList()

        val totalItems = groupedByCurrency.map { (currency, txs) ->
            val incomeSum = txs.filter { it.type == TransactionType.Income }.sumOf { it.amount }
            val expenseSum = txs.filter { it.type == TransactionType.Expense }.sumOf { it.amount }
            DsBalanceData.Total(
                currencySymbol = currency.currencySymbol,
                value = (incomeSum - expenseSum).amountToValue()
            )
        }.toImmutableList()

        return DsBalanceData(
            income = incomeItems,
            expense = expenseItems,
            total = totalItems,
        )
    }
}
