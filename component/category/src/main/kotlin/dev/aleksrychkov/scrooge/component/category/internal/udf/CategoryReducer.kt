package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.Delete
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.ObserveCategories
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand.Search
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.core.resources.R as resources

internal class CategoryReducer(
    private val resourceManager: ResourceManager = get(),
) :
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
                        val msg = String.format(
                            resourceManager.getString(resources.string.category_error_duplicate),
                            event.duplicate.name
                        )
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToCreateNewCategory -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(resources.string.category_error_failed_to_create)
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToCreateNewCategoryEmptyName -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(resources.string.category_error_empty_name)
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToDeleteCategory -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(resources.string.category_error_failed_to_delete)
                        listOf(CategoryEffect.ShowErrorMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToObserveCategories -> TODO()
        }
    }
}
