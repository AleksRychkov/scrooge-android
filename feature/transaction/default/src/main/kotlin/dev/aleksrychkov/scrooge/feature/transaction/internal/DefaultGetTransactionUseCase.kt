package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetTransactionUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetTransactionUseCase {
    override suspend fun invoke(id: Long): GetTransactionResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val entity = transactionDao.value.get(id)
                if (entity == null) {
                    GetTransactionResult.NotFound
                } else {
                    GetTransactionResult.Success(transaction = entity)
                }
            }.getOrDefault(GetTransactionResult.Failure)
        }
}
