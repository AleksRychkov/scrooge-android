package dev.aleksrychkov.scrooge.component.transactioncrud.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.transactioncrud.TransactionCrudComponent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

internal interface TransactionCrudComponentInternal : TransactionCrudComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>

    fun openCategoryModal()
    fun closeCategoryModal()
    fun setCategory(value: CategoryEntity)
}
