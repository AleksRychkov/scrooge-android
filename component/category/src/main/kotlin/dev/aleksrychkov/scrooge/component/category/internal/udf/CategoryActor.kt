package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class CategoryActor(
    private val observeCategoryUseCase: Lazy<ObserveCategoryUseCase>,
) : Actor<CategoryCommand, CategoryEvent> {

    companion object {
        operator fun invoke(): CategoryActor {
            return CategoryActor(
                observeCategoryUseCase = getLazy(),
            )
        }
    }

    override suspend fun process(command: CategoryCommand): Flow<CategoryEvent> {
        return when (command) {
            is CategoryCommand.Delete -> handleDelete(command)
            is CategoryCommand.ObserveCategories -> handleObserve(command)
            is CategoryCommand.Search -> handleSearch(command)
        }
    }

    @Suppress("UnusedParameter")
    private suspend fun handleDelete(cmd: CategoryCommand.Delete): Flow<CategoryEvent> {
        return emptyFlow()
    }

    private fun handleSearch(cmd: CategoryCommand.Search): Flow<CategoryEvent> {
        val filtered = if (cmd.query.isBlank()) {
            persistentListOf()
        } else {
            cmd.categories
                .filter {
                    it.name.lowercase().contains(cmd.query.lowercase())
                }
                .toImmutableList()
        }
        return flowOf(CategoryEvent.Internal.Filtered(list = filtered.toImmutableList()))
    }

    private suspend fun handleObserve(cmd: CategoryCommand.ObserveCategories): Flow<CategoryEvent> {
        val result = observeCategoryUseCase.value(cmd.transactionType)
        return if (result.isSuccess) {
            result
                .getOrThrow()
                .map { CategoryEvent.Internal.Categories(list = it) }
        } else {
            flowOf(CategoryEvent.Internal.FailedToObserveCategories)
        }
    }
}
