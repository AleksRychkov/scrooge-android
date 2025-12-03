package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.collections.immutable.ImmutableList

internal sealed interface TransactionsListEvent {
    sealed interface External : TransactionsListEvent {
        data class SetPeriod(val period: Pair<Long, Long>) : External
        data class SetListState(val index: Int, val offset: Int) : External
    }

    sealed interface Internal : TransactionsListEvent {
        data object FailedToLoadTransactions : TransactionsListEvent
        data class SuccessToLoadTransactions(
            val transactions: ImmutableList<TransactionEntity>,
        ) : TransactionsListEvent
    }
}
