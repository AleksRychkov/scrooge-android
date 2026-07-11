package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.GetRandomCategoryResult
import dev.aleksrychkov.scrooge.feature.category.GetRandomCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetRandomCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetRandomCategoryUseCase {
    override suspend fun invoke(transactionType: TransactionType?): GetRandomCategoryResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                categoryDao.value.getRandom(transactionType)
                    ?.let { GetRandomCategoryResult.Success(it) }
                    ?: GetRandomCategoryResult.Empty
            }.getOrDefault(GetRandomCategoryResult.Empty)
        }
}
