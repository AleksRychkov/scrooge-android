package dev.aleksrychkov.scrooge.component.transactions.internal

import dev.aleksrychkov.scrooge.component.transactions.TransactionsComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsState
import kotlinx.coroutines.flow.StateFlow

internal interface TransactionsComponentInternal : TransactionsComponent {
    val state: StateFlow<TransactionsState>

    fun addIncome()
    fun addExpense()
}
