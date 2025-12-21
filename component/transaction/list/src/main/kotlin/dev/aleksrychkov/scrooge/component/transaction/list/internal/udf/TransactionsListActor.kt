package dev.aleksrychkov.scrooge.component.transaction.list.internal.udf

import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.actors.LoadTransactionsDelegate
import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.actors.PreloadCategoriesDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow

internal class TransactionsListActor(
    private val loadTransactionsDelegate: LoadTransactionsDelegate,
    private val preloadCategoriesDelegate: PreloadCategoriesDelegate,
) : Actor<TransactionsListCommand, TransactionsListEvent> {

    companion object {
        operator fun invoke(): TransactionsListActor {
            return TransactionsListActor(
                loadTransactionsDelegate = LoadTransactionsDelegate(
                    useCase = getLazy(),
                ),
                preloadCategoriesDelegate = PreloadCategoriesDelegate(
                    useCase = getLazy(),
                )
            )
        }
    }

    override suspend fun process(command: TransactionsListCommand): Flow<TransactionsListEvent> {
        return when (command) {
            is TransactionsListCommand.LoadTransactions -> loadTransactionsDelegate(command)
            is TransactionsListCommand.PreloadCategories -> preloadCategoriesDelegate(command)
        }
    }
}
