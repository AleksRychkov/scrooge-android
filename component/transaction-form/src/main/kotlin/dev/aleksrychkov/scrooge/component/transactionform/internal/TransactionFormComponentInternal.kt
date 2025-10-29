package dev.aleksrychkov.scrooge.component.transactionform.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.component.tag.TagComponent
import dev.aleksrychkov.scrooge.component.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity

internal interface TransactionFormComponentInternal : TransactionFormComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>
    val tagModal: Value<ChildSlot<*, TagComponent>>
    val currencyModal: Value<ChildSlot<*, CurrencyComponent>>

    fun openCategoryModal()
    fun closeCategoryModal()
    fun setCategory(value: CategoryEntity)

    fun openTagModal()
    fun closeTagModal()
    fun addTag(value: TagEntity)
    fun removeTag(value: TagEntity)

    fun openCurrencyModal()
    fun closeCurrencyModal()
    fun selectCurrency(value: CurrencyEntity)
}
