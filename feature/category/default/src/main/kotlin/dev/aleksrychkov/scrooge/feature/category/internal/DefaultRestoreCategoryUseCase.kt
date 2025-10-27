package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryResult
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultRestoreCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : RestoreCategoryUseCase {

    override suspend fun invoke(
        category: CategoryEntity,
    ): RestoreCategoryResult = withContext(ioDispatcher) {
        runSuspendCatching {
            categoryDao.value.restore(category.id)
            RestoreCategoryResult.Success
        }.getOrDefault(RestoreCategoryResult.Failure)
    }
}
