package dev.aleksrychkov.scrooge.component.category.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.category.internal.component.CreateCategoryComponent
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryState
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface CategoryComponentInternal : CategoryComponent {
    val createCategoryModal: Value<ChildSlot<*, CreateCategoryComponent>>

    val state: StateFlow<CategoryState>
    val effects: Flow<CategoryEffect>

    fun deleteCategory(category: CategoryEntity)
    fun restoreCategory(category: CategoryEntity)
    fun setSearchQuery(query: String)
    fun openAddCategoryModal()
    fun closeAddCategoryModal()
}
