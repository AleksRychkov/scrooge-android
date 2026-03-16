package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransactionType

internal sealed interface CategoryCarouselCommand {
    data class ObserveCategories(val type: TransactionType) : CategoryCarouselCommand
}
