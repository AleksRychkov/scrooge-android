package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

fun interface GetMostUsedCategoryUseCase {
    suspend operator fun invoke(filter: FilterEntity): GetMostUsedCategoryResult
}

sealed interface GetMostUsedCategoryResult {
    data class Success(val category: CategoryEntity) : GetMostUsedCategoryResult
    data object Empty : GetMostUsedCategoryResult
}
