package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class LoadTransactionsDelegate(
    private val useCase: Lazy<GetTransactionsUseCase>,
) {
    suspend operator fun invoke(
        cmd: TransactionsListCommand.LoadTransactions,
    ): Flow<TransactionsListEvent> {
        return when (val res: GetTransactionsResult = useCase.value.invoke(period = cmd.period)) {
            GetTransactionsResult.Failure -> flowOf(TransactionsListEvent.Internal.FailedToLoadTransactions)
            is GetTransactionsResult.Success -> res.result.map {
                TransactionsListEvent.Internal.SuccessToLoadTransactions(it)
            }
        }
    }
}
