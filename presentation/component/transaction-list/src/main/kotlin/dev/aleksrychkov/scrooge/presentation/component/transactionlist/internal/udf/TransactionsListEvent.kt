package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import androidx.paging.PagingData
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

internal sealed interface TransactionsListEvent {
    sealed interface External : TransactionsListEvent {
        data object Initial : External
        data class SetFilter(val filter: FilterEntity) : External
    }

    sealed interface Internal : TransactionsListEvent {
        data class PagedTransactions(
            val data: Flow<PagingData<TransactionEntity>>
        ) : TransactionsListEvent
    }
}
