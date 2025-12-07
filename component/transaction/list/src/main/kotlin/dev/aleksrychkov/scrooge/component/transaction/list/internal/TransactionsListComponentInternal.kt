package dev.aleksrychkov.scrooge.component.transaction.list.internal

import dev.aleksrychkov.scrooge.component.transaction.list.TransactionsListComponent
import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.TransactionsListState
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.flow.StateFlow

internal interface TransactionsListComponentInternal : TransactionsListComponent {
    val state: StateFlow<TransactionsListState>

    fun onBackClicked()
    fun onTransactionClicked(transaction: TransactionEntity)
    fun onListStateChanged(position: Pair<Int, Int>)
}
