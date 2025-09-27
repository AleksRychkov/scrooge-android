package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

fun interface DeleteCategoryUseCase {
    suspend operator fun invoke(categoryEntity: CategoryEntity): Result<DeleteCategoryResult>
}

sealed interface DeleteCategoryResult {
    data object Success : DeleteCategoryResult
    data class NotUserMadeViolation(val categoryEntity: CategoryEntity) : DeleteCategoryResult
}
