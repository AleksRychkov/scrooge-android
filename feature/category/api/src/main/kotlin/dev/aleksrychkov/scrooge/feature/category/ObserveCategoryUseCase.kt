package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface ObserveCategoryUseCase {
    suspend operator fun invoke(
        transactionType: TransactionType,
    ): Result<Flow<ImmutableList<CategoryEntity>>>
}
