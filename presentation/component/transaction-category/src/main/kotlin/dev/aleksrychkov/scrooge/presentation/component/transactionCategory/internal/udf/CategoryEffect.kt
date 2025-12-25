package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

internal sealed interface CategoryEffect {
    data class ShowInfoMessage(val message: String) : CategoryEffect
    data class CategoryDeleted(
        val message: String,
        val actionLabel: String,
        val category: CategoryEntity,
    ) : CategoryEffect
}
