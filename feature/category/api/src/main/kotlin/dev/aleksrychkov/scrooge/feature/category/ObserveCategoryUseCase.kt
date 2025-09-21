package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.common.entity.CategoryEntity
import dev.aleksrychkov.scrooge.common.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface ObserveCategoryUseCase {
    suspend operator fun invoke(
        transactionType: TransactionType,
    ): Result<Flow<ImmutableList<CategoryEntity>>>
}
