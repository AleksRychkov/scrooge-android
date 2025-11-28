package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
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
                    transactions = entry.value.toImmutableList(),
                )
            }
            .toImmutableList()
    }
}
