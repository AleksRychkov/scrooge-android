package dev.aleksrychkov.scrooge.presentation.screen.transactions.internal

import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactions.TransactionsComponent

internal interface TransactionsComponentInternal : TransactionsComponent {
    val transactionsListComponent: TransactionsListComponent

    fun onBackPressed()
}
