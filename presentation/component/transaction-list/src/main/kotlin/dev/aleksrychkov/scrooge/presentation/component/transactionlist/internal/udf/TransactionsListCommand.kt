package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface TransactionsListCommand {
    data class LoadTransactions(val filter: FilterEntity) : TransactionsListCommand
    data object PreloadCategories : TransactionsListCommand
}
