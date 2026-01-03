package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

fun interface GetCategoryUseCase {
    suspend operator fun invoke(id: Long): GetCategoryResult
}

sealed interface GetCategoryResult {
    data class Success(val entity: CategoryEntity) : GetCategoryResult
    data object NotFound : GetCategoryResult
    data object Failure : GetCategoryResult
}
