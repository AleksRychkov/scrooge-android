package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.actors.LoadTransactionsDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.actors.PreloadCategoriesDelegate
import kotlinx.coroutines.flow.Flow

internal class TransactionsListActor(
    private val loadTransactionsDelegate: LoadTransactionsDelegate = LoadTransactionsDelegate(),
    private val preloadCategoriesDelegate: PreloadCategoriesDelegate = PreloadCategoriesDelegate(),
) : Actor<TransactionsListCommand, TransactionsListEvent> {
    override suspend fun process(command: TransactionsListCommand): Flow<TransactionsListEvent> {
        return when (command) {
            is TransactionsListCommand.LoadTransactions -> loadTransactionsDelegate(command)
            is TransactionsListCommand.PreloadCategories -> preloadCategoriesDelegate(command)
        }
    }
}
