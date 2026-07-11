package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

fun interface GetRandomCategoryUseCase {
    suspend operator fun invoke(transactionType: TransactionType?): GetRandomCategoryResult
}

sealed interface GetRandomCategoryResult {
    data class Success(val category: CategoryEntity) : GetRandomCategoryResult
    data object Empty : GetRandomCategoryResult
}
