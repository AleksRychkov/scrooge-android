package dev.aleksrychkov.scrooge.component.transactioncrud.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.tag.TagComponent
import dev.aleksrychkov.scrooge.component.transactioncrud.TransactionCrudComponent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity

internal interface TransactionCrudComponentInternal : TransactionCrudComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>
    val tagModal: Value<ChildSlot<*, TagComponent>>

    fun openCategoryModal()
    fun closeCategoryModal()
    fun setCategory(value: CategoryEntity)

    fun openTagModal()
    fun closeTagModal()
    fun addTag(value: TagEntity)
    fun removeTag(value: TagEntity)
}
