package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

fun interface RestoreCategoryUseCase {
    suspend operator fun invoke(category: CategoryEntity): RestoreCategoryResult
}

sealed interface RestoreCategoryResult {
    data object Success : RestoreCategoryResult
    data object Failure : RestoreCategoryResult
}
