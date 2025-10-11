package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.Delete
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.ObserveCategories
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.Search
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class CategoryReducer : Reducer<CategoryState, CategoryEvent, CategoryCommand, Unit> {

    override fun reduce(
        event: CategoryEvent,
        state: CategoryState
    ): ReducerResult<CategoryState, CategoryCommand, Unit> {
        return when (event) {
            is CategoryEvent.External.Delete -> {
                state.reduceWith(event) {
                    command {
                        listOf(Delete(event.categoryId))
                    }
                }
            }

            is CategoryEvent.External.Init -> {
                state.reduceWith(event) {
                    command {
                        listOf(ObserveCategories(transactionType = event.transactionType))
                    }
                    state {
                        copy(transactionType = event.transactionType)
                    }
                }
            }

            is CategoryEvent.External.Search -> {
                state.reduceWith(event) {
                    command {
                        listOf(Search(query = event.query.trim(), categories = categories.toList()))
                    }
                    state {
                        copy(searchQuery = event.query.trim())
                    }
                }
            }

            is CategoryEvent.Internal.Categories -> {
                state.reduceWith(event) {
                    state {
                        copy(categories = event.list)
                    }
                }
            }

            is CategoryEvent.Internal.Filtered -> {
                state.reduceWith(event) {
                    state {
                        copy(filtered = event.list)
                    }
                }
            }

            CategoryEvent.Internal.FailedToObserveCategories -> TODO()
        }
    }
}
