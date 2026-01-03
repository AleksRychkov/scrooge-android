package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.GetCategoryResult
import dev.aleksrychkov.scrooge.feature.category.GetCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetCategoryUseCase {

    override suspend fun invoke(id: Long): GetCategoryResult = withContext(ioDispatcher) {
        runSuspendCatching {
            val entity = categoryDao.value.get(id)
            if (entity == null) {
                GetCategoryResult.NotFound
            } else {
                GetCategoryResult.Success(entity = entity)
            }
        }.getOrDefault(GetCategoryResult.Failure)
    }
}
