package dev.aleksrychkov.scrooge.component.transactions.internal

import dev.aleksrychkov.scrooge.component.transactions.TransactionsComponent

internal interface TransactionsComponentInternal : TransactionsComponent {
    fun addIncome()
    fun addExpense()
}
