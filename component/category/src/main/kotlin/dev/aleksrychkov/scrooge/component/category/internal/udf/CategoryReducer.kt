package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.Delete
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.ObserveCategories
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.Search
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class CategoryReducer :
    Reducer<CategoryState, CategoryEvent, CategoryCommand, CategoryEffect> {

    @Suppress("LongMethod")
    override fun reduce(
        event: CategoryEvent,
        state: CategoryState
    ): ReducerResult<CategoryState, CategoryCommand, CategoryEffect> {
        return when (event) {
            is CategoryEvent.External.Delete -> {
                state.reduceWith(event) {
                    command {
                        listOf(Delete(category = event.category))
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
                        listOf(Search(query = event.query, categories = categories.toList()))
                    }
                    state {
                        copy(searchQuery = event.query)
                    }
                }
            }

            CategoryEvent.External.AddNewCategory -> {
                state.reduceWith(event) {
                    command {
                        listOf(
                            CategoryCommand.CreateNewCategory(
                                name = state.searchQuery,
                                transactionType = state.transactionType,
                            )
                        )
                    }
                }
            }

            is CategoryEvent.Internal.Categories -> {
                state.reduceWith(event) {
                    if (state.searchQuery.isNotBlank()) {
                        command {
                            listOf(Search(query = state.searchQuery, categories = event.list))
                        }
                    }
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

            is CategoryEvent.Internal.FailedToCreateNewCategoryDuplicate -> {
                state.reduceWith(event) {
                    effects {
                        // todo text from resources
                        val msg = "Category with name \"${event.duplicate.name}\" already exists"
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToCreateNewCategory -> {
                state.reduceWith(event) {
                    effects {
                        // todo text from resources
                        val msg = "Failed to create category"
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToCreateNewCategoryEmptyName -> {
                state.reduceWith(event) {
                    effects {
                        // todo text from resources
                        val msg = "Type category name to create new one"
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToDeleteCategory -> {
                state.reduceWith(event) {
                    effects {
                        // todo text from resources
                        val msg = "Failed to delete category"
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToObserveCategories -> TODO()
        }
    }
}
