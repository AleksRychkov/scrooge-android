package dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryEvent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class ObserveCategoryDelegate(
    private val observeCategoryUseCase: Lazy<ObserveCategoryUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: CategoryCommand.ObserveCategories): Flow<CategoryEvent> {
        return if (cmd.transactionType != null) {
            observeByType(cmd.transactionType)
        } else {
            observeAll()
        }
    }

    private suspend fun observeByType(type: TransactionType): Flow<CategoryEvent> {
        val result = observeCategoryUseCase.value(type)
        return if (result is ObserveCategoryResult.Success) {
            result
                .categories
                .map {
                    CategoryEvent.Internal.Categories(
                        list = it,
                        hash = it.sortedBy(CategoryEntity::id).hashCode(),
                    )
                }
        } else {
            emptyFlow()
        }
    }

    private suspend fun observeAll(): Flow<CategoryEvent> {
        val income =
            (observeCategoryUseCase.value(TransactionType.Income) as? ObserveCategoryResult.Success)
                ?.categories
                ?: emptyFlow()
        val expense =
            (observeCategoryUseCase.value(TransactionType.Expense) as? ObserveCategoryResult.Success)
                ?.categories
                ?: emptyFlow()

        return combine(income, expense) {
            it.toList().flatten().toImmutableList()
        }
            .map {
                CategoryEvent.Internal.Categories(
                    list = it,
                    hash = it.sortedBy(CategoryEntity::id).hashCode(),
                )
            }
    }
}
