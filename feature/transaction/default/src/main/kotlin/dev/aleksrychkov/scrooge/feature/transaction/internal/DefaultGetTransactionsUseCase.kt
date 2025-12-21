package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetTransactionsUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetTransactionsUseCase {
    override suspend fun invoke(period: PeriodTimestampEntity): GetTransactionsResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val flow = transactionDao.value.get(period = period)
                GetTransactionsResult.Success(result = flow)
            }.getOrDefault(GetTransactionsResult.Failure)
        }
}
