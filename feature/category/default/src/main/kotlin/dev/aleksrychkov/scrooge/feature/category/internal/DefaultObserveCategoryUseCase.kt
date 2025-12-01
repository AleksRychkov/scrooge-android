package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import dev.aleksrychkov.scrooge.feature.category.internal.source.CategoryKeyValueSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultObserveCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultCategories: DefaultCategories,
    private val keyValueSource: Lazy<CategoryKeyValueSource>,
) : ObserveCategoryUseCase {
    override suspend fun invoke(
        transactionType: TransactionType,
    ): ObserveCategoryResult =
        withContext(ioDispatcher) {
            preloadDefaultCategoriesIfNeeded()
            runSuspendCatching {
                val categories = categoryDao.value.get(transactionType)
                ObserveCategoryResult.Success(categories = categories)
            }.getOrDefault(ObserveCategoryResult.Failure)
        }

    private suspend fun preloadDefaultCategoriesIfNeeded() = runSuspendCatching {
        val isPreloadNeeded = !keyValueSource.value.isPreloadDone()
        if (!isPreloadNeeded) return@runSuspendCatching
        defaultCategories.get(TransactionType.Expense).forEach { entity ->
            categoryDao.value.create(
                name = entity.name,
                type = TransactionType.Expense,
                iconId = entity.iconId,
            )
        }
        defaultCategories.get(TransactionType.Income).forEach { entity ->
            categoryDao.value.create(
                name = entity.name,
                type = TransactionType.Income,
                iconId = entity.iconId,
            )
        }
        keyValueSource.value.setPreloadDone()
    }
}
