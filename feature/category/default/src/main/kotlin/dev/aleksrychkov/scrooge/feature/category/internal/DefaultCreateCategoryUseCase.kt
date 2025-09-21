package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.common.database.CategoryDao
import dev.aleksrychkov.scrooge.common.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultCreateCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : CreateCategoryUseCase {
    override suspend fun invoke(
        categoryEntity: CategoryEntity,
    ): Result<CreateCategoryResult> =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val duplicateCategory = categoryDao.value.getByName(
                    name = categoryEntity.name,
                    type = categoryEntity.type,
                )
                if (duplicateCategory != null) {
                    CreateCategoryResult.DuplicateViolation(categoryEntity = duplicateCategory)
                } else {
                    categoryDao.value.create(
                        name = categoryEntity.name,
                        type = categoryEntity.type,
                    )
                    CreateCategoryResult.Success
                }
            }
        }
}
