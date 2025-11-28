package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal class TransactionsListMapper {

    fun transactionsToDayTransactions(
        transactions: List<TransactionEntity>,
    ): ImmutableList<TransactionsGroup> {
        return transactions
            .groupBy { transaction ->
                val timeZone = TimeZone.currentSystemDefault()
                val instant = Instant.fromEpochMilliseconds(transaction.timestamp)
                instant.toLocalDateTime(timeZone).date
            }
            .map { entry ->
                TransactionsGroup(
                    date = "${entry.key.day}.${entry.key.month.number}.${entry.key.year}",
                    totals = calculateTotals(entry.value),
                    transactions = entry.value.toImmutableList(),
                )
            }
            .toImmutableList()
    }

    private fun calculateTotals(transactions: List<TransactionEntity>): ImmutableList<String> {
        return transactions
            .groupBy {
                it.currency
            }
            .map { entry ->
                val total =
                    entry.value.sumOf { it.amount * (if (it.type == TransactionType.Expense) -1 else 1) }
                val sign = if (total > 0) "+" else ""
                "$sign${total.amountToValue()} ${entry.key.currencySymbol}"
            }
            .toImmutableList()
    }
}
