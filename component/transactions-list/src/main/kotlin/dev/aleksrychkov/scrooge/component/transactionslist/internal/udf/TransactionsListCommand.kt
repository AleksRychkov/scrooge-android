package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

internal sealed interface TransactionsListCommand {
    data class LoadTransactions(
        val period: Pair<Long, Long>,
    ) : TransactionsListCommand
}
