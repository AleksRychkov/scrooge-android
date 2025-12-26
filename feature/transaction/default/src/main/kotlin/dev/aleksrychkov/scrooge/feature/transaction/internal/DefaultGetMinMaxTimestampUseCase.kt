package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetMinMaxTimestampUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetMinMaxTimestampUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetMinMaxTimestampUseCase {
    override suspend fun invoke(): PeriodTimestampEntity? = withContext(ioDispatcher) {
        runSuspendCatching {
            transactionDao.value.getMinMaxTimestamp()
        }.getOrNull()
    }
}
