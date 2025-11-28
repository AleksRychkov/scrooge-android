package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.DeleteTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.DeleteTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultDeleteTransactionUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : DeleteTransactionUseCase {
    override suspend fun invoke(transactionId: Long): DeleteTransactionResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.delete(id = transactionId)
                DeleteTransactionResult.Success
            }.getOrDefault(DeleteTransactionResult.Failure)
        }
}
