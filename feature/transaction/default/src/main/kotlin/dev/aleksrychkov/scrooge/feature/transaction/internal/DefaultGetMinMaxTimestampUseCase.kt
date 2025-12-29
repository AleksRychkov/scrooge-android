package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetMinMaxTimestampUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetMinMaxTimestampUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetMinMaxTimestampUseCase {
    override suspend fun invoke(): PeriodDatestampEntity? = withContext(ioDispatcher) {
        runSuspendCatching {
            transactionDao.value.getMinMaxDatestamp()
        }.getOrNull()
    }
}
