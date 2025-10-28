package dev.aleksrychkov.scrooge.component.category.internal

import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryState
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface CategoryComponentInternal : CategoryComponent {
    val state: StateFlow<CategoryState>
    val effects: Flow<CategoryEffect>

    fun deleteCategory(category: CategoryEntity)
    fun restoreCategory(category: CategoryEntity)
    fun setSearchQuery(query: String)
    fun addNewCategory()
}
