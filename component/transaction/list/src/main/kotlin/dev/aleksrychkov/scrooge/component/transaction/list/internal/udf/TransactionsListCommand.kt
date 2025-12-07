package dev.aleksrychkov.scrooge.component.transaction.list.internal.udf

internal sealed interface TransactionsListCommand {
    data class LoadTransactions(
        val period: Pair<Long, Long>,
    ) : TransactionsListCommand
}
