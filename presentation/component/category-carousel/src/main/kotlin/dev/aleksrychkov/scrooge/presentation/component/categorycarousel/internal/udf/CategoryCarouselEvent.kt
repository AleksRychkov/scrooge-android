package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList

internal sealed interface CategoryCarouselEvent {
    sealed interface External : CategoryCarouselEvent {
        data class ObserveCategories(val type: TransactionType) : External
        data class SelectCategory(val category: CategoryEntity) : External
    }

    sealed interface Internal : CategoryCarouselEvent {
        data class CategoriesResult(val categories: ImmutableList<CategoryEntity>) : Internal
    }
}
