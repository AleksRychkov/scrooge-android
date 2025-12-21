package dev.aleksrychkov.scrooge.component.transaction.list.internal.udf

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

internal sealed interface TransactionsListCommand {
    data class LoadTransactions(
        val period: PeriodTimestampEntity,
    ) : TransactionsListCommand

    data object PreloadCategories : TransactionsListCommand
}
