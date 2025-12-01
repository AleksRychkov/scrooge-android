package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryResult
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultDeleteCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : DeleteCategoryUseCase {
    override suspend fun invoke(
        categoryEntity: CategoryEntity,
    ): DeleteCategoryResult = withContext(ioDispatcher) {
        runSuspendCatching {
            categoryDao.value.delete(categoryEntity.id)
            DeleteCategoryResult.Success
        }.getOrDefault(DeleteCategoryResult.Failure)
    }
}
