package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.common.entity.CategoryEntity

fun interface CreateCategoryUseCase {
    suspend operator fun invoke(
        categoryEntity: CategoryEntity,
    ): Result<CreateCategoryResult>
}

sealed interface CreateCategoryResult {
    data object Success : CreateCategoryResult
    data class DuplicateViolation(val categoryEntity: CategoryEntity) : CreateCategoryResult
}
