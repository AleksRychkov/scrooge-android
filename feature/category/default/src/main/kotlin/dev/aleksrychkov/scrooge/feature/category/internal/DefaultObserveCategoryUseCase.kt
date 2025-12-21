package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultObserveCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ObserveCategoryUseCase {
    override suspend fun invoke(
        transactionType: TransactionType,
    ): ObserveCategoryResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val categories = categoryDao.value.get(transactionType)
                ObserveCategoryResult.Success(categories = categories)
            }.getOrDefault(ObserveCategoryResult.Failure)
        }
}
