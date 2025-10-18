package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultCreateCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultCategories: DefaultCategories,
) : CreateCategoryUseCase {
    override suspend fun invoke(
        categoryEntity: CategoryEntity,
    ): CreateCategoryResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val defaultDuplicateCategory = defaultCategories.get(categoryEntity.type)
                    .find { it.name.contains(categoryEntity.name) }
                if (defaultDuplicateCategory != null) {
                    return@runSuspendCatching CreateCategoryResult.DuplicateViolation(
                        categoryEntity = defaultDuplicateCategory,
                    )
                }

                val duplicateCategory = categoryDao.value.getByName(
                    name = categoryEntity.name,
                    type = categoryEntity.type,
                )
                if (duplicateCategory != null) {
                    return@runSuspendCatching CreateCategoryResult.DuplicateViolation(
                        categoryEntity = duplicateCategory,
                    )
                }

                categoryDao.value.create(
                    name = categoryEntity.name,
                    type = categoryEntity.type,
                )
                CreateCategoryResult.Success
            }.getOrDefault(CreateCategoryResult.Failure)
        }
}
