package dev.aleksrychkov.scrooge.component.transactions.internal.udf

internal sealed interface TransactionsCommand {
    data object Loading : TransactionsCommand
}
