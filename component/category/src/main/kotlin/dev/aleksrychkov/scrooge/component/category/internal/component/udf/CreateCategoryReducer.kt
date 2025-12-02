package dev.aleksrychkov.scrooge.component.category.internal.component.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class CreateCategoryReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<CreateCategoryState, CreateCategoryEvent, CreateCategoryCommand, CreateCategoryEffect> {
    override fun reduce(
        event: CreateCategoryEvent,
        state: CreateCategoryState
    ): ReducerResult<CreateCategoryState, CreateCategoryCommand, CreateCategoryEffect> {
        return when (event) {
            CreateCategoryEvent.External.Init -> state.reduceWith(event) {
                state {
                    val icons = listOf(UncategorizedIcon) + CategoryIcons
                    copy(
                        isLoading = false,
                        availableIcons = icons.toImmutableList(),
                    )
                }
            }

            is CreateCategoryEvent.External.SetIcon -> state.reduceWith(event) {
                state {
                    copy(selectedCategoryIcon = event.icon)
                }
            }

            is CreateCategoryEvent.External.SetName -> state.reduceWith(event) {
                state {
                    copy(name = event.name)
                }
            }

            CreateCategoryEvent.External.Submit -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(CreateCategoryCommand.Submit(state.copy()))
                }
            }

            CreateCategoryEvent.Internal.Failure -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager
                        .getString(Resources.string.category_error_failed_to_create)
                    listOf(CreateCategoryEffect.ShowInfoMessage(msg))
                }
            }

            is CreateCategoryEvent.Internal.DuplicateError -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = String.format(
                        resourceManager.getString(Resources.string.category_error_duplicate),
                        event.duplicate.name
                    )
                    listOf(CreateCategoryEffect.ShowInfoMessage(msg))
                }
            }

            CreateCategoryEvent.Internal.EmptyNameError -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager
                        .getString(Resources.string.category_error_empty_name)
                    listOf(CreateCategoryEffect.ShowInfoMessage(msg))
                }
            }

            CreateCategoryEvent.Internal.Success -> state.reduceWith(event) {
                state {
                    copy(isLoading = false, isDone = true)
                }
            }
        }
    }
}
