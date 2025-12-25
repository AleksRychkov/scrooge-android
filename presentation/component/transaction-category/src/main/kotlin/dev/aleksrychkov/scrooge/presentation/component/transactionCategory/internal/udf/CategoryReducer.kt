package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf

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

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun reduce(
        event: CategoryEvent,
        state: CategoryState
    ): ReducerResult<CategoryState, CategoryCommand, CategoryEffect> {
        return when (event) {
            is CategoryEvent.External.Delete -> {
                state.reduceWith(event) {
                    command {
                        listOf(CategoryCommand.Delete(category = event.category))
                    }
                }
            }

            is CategoryEvent.External.Restore -> {
                state.reduceWith(event) {
                    command {
                        listOf(CategoryCommand.Restore(category = event.category))
                    }
                }
            }

            is CategoryEvent.External.Init -> {
                state.reduceWith(event) {
                    command {
                        listOf(CategoryCommand.ObserveCategories(transactionType = event.transactionType))
                    }
                    state {
                        copy(transactionType = event.transactionType)
                    }
                }
            }

            is CategoryEvent.External.Search -> {
                state.reduceWith(event) {
                    command {
                        listOf(
                            CategoryCommand.Search(
                                query = event.query,
                                categories = categories.toList()
                            )
                        )
                    }
                    state {
                        copy(searchQuery = event.query)
                    }
                }
            }

            is CategoryEvent.Internal.Categories -> {
                state.reduceWith(event) {
                    if (state.searchQuery.isNotBlank()) {
                        command {
                            listOf(
                                CategoryCommand.Search(
                                    query = state.searchQuery,
                                    categories = event.list
                                )
                            )
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

            CategoryEvent.Internal.FailedToDeleteCategory -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(
                                resources.string.category_error_failed_to_delete
                            )
                        listOf(CategoryEffect.ShowInfoMessage(msg))
                    }
                }
            }

            CategoryEvent.Internal.FailedToRestoreCategory -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(
                                resources.string.category_error_failed_to_restore
                            )
                        listOf(CategoryEffect.ShowInfoMessage(msg))
                    }
                }
            }

            is CategoryEvent.Internal.DeletedCategory -> {
                state.reduceWith(event) {
                    effects {
                        val msg = String.format(
                            resourceManager.getString(resources.string.category_deleted),
                            event.category.name
                        )
                        listOf(
                            CategoryEffect.CategoryDeleted(
                                message = msg,
                                actionLabel = resourceManager.getString(
                                    resources.string.undo
                                ),
                                category = event.category,
                            )
                        )
                    }
                }
            }

            CategoryEvent.Internal.FailedToObserveCategories -> TODO()
        }
    }
}
