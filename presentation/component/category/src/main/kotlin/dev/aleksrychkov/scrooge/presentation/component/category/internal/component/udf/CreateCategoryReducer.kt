package dev.aleksrychkov.scrooge.presentation.component.category.internal.component.udf

import androidx.compose.ui.graphics.toArgb
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.CategoryColors
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.resources.categoryIconFromId
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class CreateCategoryReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<CreateCategoryState, CreateCategoryEvent, CreateCategoryCommand, CreateCategoryEffect> {

    @Suppress("LongMethod")
    override fun reduce(
        event: CreateCategoryEvent,
        state: CreateCategoryState
    ): ReducerResult<CreateCategoryState, CreateCategoryCommand, CreateCategoryEffect> {
        return when (event) {
            is CreateCategoryEvent.External.Init -> state.reduceWith(event) {
                if (state.id != null) {
                    command {
                        listOf(CreateCategoryCommand.Load(id = state.id))
                    }
                }
                state {
                    copy(
                        isLoading = state.id != null,
                        availableIcons = CategoryIcons.toImmutableList(),
                        availableColors = CategoryColors
                            .map { CreateCategoryState.ColorWrapper(it, it.toArgb()) }
                            .toImmutableList()
                    )
                }
            }

            is CreateCategoryEvent.External.SetIcon -> state.reduceWith(event) {
                state {
                    copy(selectedCategoryIcon = event.icon)
                }
            }

            is CreateCategoryEvent.External.SetColor -> state.reduceWith(event) {
                state {
                    copy(selectedCategoryColor = event.color)
                }
            }

            is CreateCategoryEvent.External.SetName -> state.reduceWith(event) {
                state {
                    copy(name = event.name)
                }
            }

            CreateCategoryEvent.External.Submit -> state.reduceWith(event) {
                state {
                    copy(isLoading = true, name = state.name.trim())
                }
                command {
                    listOf(CreateCategoryCommand.Submit(state.copy()))
                }
            }

            CreateCategoryEvent.Internal.FailureSubmit -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msgId = if (state.id == null) {
                        Resources.string.category_error_failed_to_create
                    } else {
                        Resources.string.category_error_failed_to_update
                    }
                    listOf(CreateCategoryEffect.ShowInfoMessage(resourceManager.getString(msgId)))
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

            CreateCategoryEvent.Internal.SuccessSubmit -> state.reduceWith(event) {
                state {
                    copy(isLoading = false, isDone = true)
                }
            }

            is CreateCategoryEvent.Internal.SuccessLoad -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        name = event.entity.name,
                        selectedCategoryColor = event.entity.color,
                        selectedCategoryIcon = categoryIconFromId(event.entity.iconId),
                    )
                }
            }

            CreateCategoryEvent.Internal.FailedLoad -> state.reduceWith(event) {
                state {
                    copy(isLoading = false, id = null)
                }
            }
        }
    }
}
