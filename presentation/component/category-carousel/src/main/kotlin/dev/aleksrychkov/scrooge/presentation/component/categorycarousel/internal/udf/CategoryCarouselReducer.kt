package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselCommand.ObserveCategories
import kotlinx.collections.immutable.toImmutableList

internal class CategoryCarouselReducer(
    private val isEditing: Boolean,
    private val callback: (CategoryEntity) -> Unit,
) :
    Reducer<CategoryCarouselState, CategoryCarouselEvent, CategoryCarouselCommand, Unit> {
    override fun reduce(
        event: CategoryCarouselEvent,
        state: CategoryCarouselState
    ): ReducerResult<CategoryCarouselState, CategoryCarouselCommand, Unit> {
        return when (event) {
            is CategoryCarouselEvent.External.ObserveCategories -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(ObserveCategories(type = event.type))
                }
            }

            is CategoryCarouselEvent.Internal.CategoriesResult -> state.reduceWith(event) {
                if (!isEditing) {
                    event.categories.firstOrNull()?.let(callback)
                }
                state {
                    copy(
                        isLoading = false,
                        carousel = event.categories.map(CarouselItem::map).toImmutableList(),
                    )
                }
            }

            is CategoryCarouselEvent.External.SelectCategory -> state.reduceWith(event) {
                callback(event.category)
                state {
                    copy(selectedCategory = event.category)
                }
            }
        }
    }
}
