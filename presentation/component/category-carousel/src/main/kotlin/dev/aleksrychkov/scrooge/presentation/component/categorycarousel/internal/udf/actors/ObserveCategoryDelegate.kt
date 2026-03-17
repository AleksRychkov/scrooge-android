package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselCommand
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselEvent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class ObserveCategoryDelegate(
    private val observeCategoryUseCase: Lazy<ObserveCategoryUseCase> = getLazy(),
) {

    suspend operator fun invoke(
        cmd: CategoryCarouselCommand.ObserveCategories,
    ): Flow<CategoryCarouselEvent> {
        return observeByType(type = cmd.type)
    }

    private suspend fun observeByType(type: TransactionType): Flow<CategoryCarouselEvent> {
        val result = observeCategoryUseCase.value(type)
        return if (result is ObserveCategoryResult.Success) {
            result
                .categories
                .map { CategoryCarouselEvent.Internal.CategoriesResult(categories = it) }
        } else {
            flowOf(CategoryCarouselEvent.Internal.CategoriesResult(categories = persistentListOf()))
        }
    }
}
