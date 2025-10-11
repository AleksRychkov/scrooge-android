package dev.aleksrychkov.scrooge.component.category.internal

import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryState
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import kotlinx.coroutines.flow.StateFlow

internal interface CategoryComponentInternal : CategoryComponent {
    val state: StateFlow<CategoryState>

    fun deleteCategory(categoryEntity: CategoryEntity)

    fun setSearchQuery(query: String)
}
