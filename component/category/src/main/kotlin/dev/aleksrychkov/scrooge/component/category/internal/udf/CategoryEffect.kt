package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

internal sealed interface CategoryEffect {
    data class ShowInfoMessage(val message: String) : CategoryEffect
    data class CategoryDeleted(
        val message: String,
        val actionLabel: String,
        val category: CategoryEntity,
    ) : CategoryEffect
}
