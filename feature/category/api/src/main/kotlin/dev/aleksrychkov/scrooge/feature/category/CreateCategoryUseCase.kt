package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

fun interface CreateCategoryUseCase {
    suspend operator fun invoke(
        categoryEntity: CategoryEntity,
    ): CreateCategoryResult
}

sealed interface CreateCategoryResult {
    data object Success : CreateCategoryResult
    data class DuplicateViolation(val categoryEntity: CategoryEntity) : CreateCategoryResult
    data object Failure : CreateCategoryResult
}
