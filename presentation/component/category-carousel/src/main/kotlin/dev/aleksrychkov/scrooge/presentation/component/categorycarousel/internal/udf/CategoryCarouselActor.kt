package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.actors.ObserveCategoryDelegate
import kotlinx.coroutines.flow.Flow

internal class CategoryCarouselActor(
    private val observeDelegate: ObserveCategoryDelegate = ObserveCategoryDelegate(),
) : Actor<CategoryCarouselCommand, CategoryCarouselEvent> {

    override suspend fun process(command: CategoryCarouselCommand): Flow<CategoryCarouselEvent> {
        return when (command) {
            is CategoryCarouselCommand.ObserveCategories -> observeDelegate(command)
        }
    }
}
