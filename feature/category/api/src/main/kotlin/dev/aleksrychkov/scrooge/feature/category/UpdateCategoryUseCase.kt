package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

fun interface UpdateCategoryUseCase {
    suspend operator fun invoke(
        categoryEntity: CategoryEntity,
    ): UpdateCategoryResult
}

sealed interface UpdateCategoryResult {
    data object Success : UpdateCategoryResult
    data class DuplicateViolation(val categoryEntity: CategoryEntity) : UpdateCategoryResult
    data object Failure : UpdateCategoryResult
}
