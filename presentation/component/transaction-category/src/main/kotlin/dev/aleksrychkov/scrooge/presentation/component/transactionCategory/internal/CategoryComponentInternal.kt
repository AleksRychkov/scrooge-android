package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.CreateCategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface CategoryComponentInternal : CategoryComponent {
    val createCategoryModal: Value<ChildSlot<*, CreateCategoryComponent>>

    val state: StateFlow<CategoryState>
    val effects: Flow<CategoryEffect>

    fun deleteCategory(category: CategoryEntity)
    fun setSearchQuery(query: String)
    fun openAddCategoryModal()
    fun closeAddCategoryModal()
}
