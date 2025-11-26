package dev.aleksrychkov.scrooge.component.transactionslist.internal

import dev.aleksrychkov.scrooge.component.transactionslist.TransactionsListComponent
import dev.aleksrychkov.scrooge.component.transactionslist.internal.udf.TransactionsListState
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.flow.StateFlow

internal interface TransactionsListComponentInternal : TransactionsListComponent {
    val state: StateFlow<TransactionsListState>

    fun onBackClicked()
    fun onTransactionClicked(transaction: TransactionEntity)
}
