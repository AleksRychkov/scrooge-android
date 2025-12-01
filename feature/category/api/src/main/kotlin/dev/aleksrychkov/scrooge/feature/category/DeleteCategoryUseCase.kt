package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

fun interface DeleteCategoryUseCase {
    suspend operator fun invoke(categoryEntity: CategoryEntity): DeleteCategoryResult
}

sealed interface DeleteCategoryResult {
    data object Success : DeleteCategoryResult
    data object Failure : DeleteCategoryResult
}
