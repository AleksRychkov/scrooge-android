package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.collections.immutable.ImmutableList

internal sealed interface TransactionsListEvent {
    sealed interface External : TransactionsListEvent {
        data object Initial : External
        data class SetFilter(val filter: FilterEntity) : External
        data class SetListState(val index: Int, val offset: Int) : External
    }

    sealed interface Internal : TransactionsListEvent {
        data object FailedToLoadTransactions : TransactionsListEvent
        data class SuccessToLoadTransactions(
            val transactions: ImmutableList<TransactionEntity>,
        ) : TransactionsListEvent
    }
}
