package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.UpdateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.UpdateCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultUpdateCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : UpdateCategoryUseCase {
    override suspend fun invoke(
        categoryEntity: CategoryEntity,
    ): UpdateCategoryResult = withContext(ioDispatcher) {
        runSuspendCatching {
            val duplicateCategory = categoryDao.value.getByName(
                name = categoryEntity.name.trim(),
                type = categoryEntity.type,
            )
            if (duplicateCategory != null && duplicateCategory.id != categoryEntity.id) {
                return@runSuspendCatching UpdateCategoryResult.DuplicateViolation(
                    categoryEntity = duplicateCategory,
                )
            }
            categoryDao.value.update(
                id = categoryEntity.id,
                name = categoryEntity.name,
                type = categoryEntity.type,
                iconId = categoryEntity.iconId,
                color = categoryEntity.color,
            )
            UpdateCategoryResult.Success
        }
            .getOrDefault(UpdateCategoryResult.Failure)
    }
}
