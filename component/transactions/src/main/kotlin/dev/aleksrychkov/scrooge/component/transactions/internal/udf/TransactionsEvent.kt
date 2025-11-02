package dev.aleksrychkov.scrooge.component.transactions.internal.udf

internal sealed interface TransactionsEvent {
    sealed interface External : TransactionsEvent {
        data object Init : External
    }

    sealed interface Internal : TransactionsEvent {
        data object Finish : Internal
    }
}
