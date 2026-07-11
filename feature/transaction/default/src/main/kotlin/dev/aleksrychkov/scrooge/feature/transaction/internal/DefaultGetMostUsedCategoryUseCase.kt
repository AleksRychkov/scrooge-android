package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCategoryResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetMostUsedCategoryUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetMostUsedCategoryUseCase {
    override suspend fun invoke(filter: FilterEntity): GetMostUsedCategoryResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.getMostUsedCategory(filter.copy(category = null))
                    ?.let { GetMostUsedCategoryResult.Success(it) }
                    ?: GetMostUsedCategoryResult.Empty
            }.getOrDefault(GetMostUsedCategoryResult.Empty)
        }
}
