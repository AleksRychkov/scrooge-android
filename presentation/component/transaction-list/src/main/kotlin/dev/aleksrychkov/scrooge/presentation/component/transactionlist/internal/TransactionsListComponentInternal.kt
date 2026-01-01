package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal

import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListState
import kotlinx.coroutines.flow.StateFlow

internal interface TransactionsListComponentInternal : TransactionsListComponent {
    val state: StateFlow<TransactionsListState>

    fun onBackClicked()
    fun onTransactionClicked(id: Long, type: TransactionType)
}
