package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.transaction.GetPagedTransactionsUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadTransactionsDelegate(
    private val useCase: Lazy<GetPagedTransactionsUseCase> = getLazy(),
) {
    suspend operator fun invoke(
        cmd: TransactionsListCommand.LoadTransactions,
    ): Flow<TransactionsListEvent> {
        return useCase.value.invoke(filter = cmd.filter).let {
            flowOf(TransactionsListEvent.Internal.PagedTransactions(it))
        }
    }
}
