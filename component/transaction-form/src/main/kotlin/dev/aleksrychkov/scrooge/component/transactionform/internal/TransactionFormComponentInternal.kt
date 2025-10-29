package dev.aleksrychkov.scrooge.component.transactionform.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.component.tag.TagComponent
import dev.aleksrychkov.scrooge.component.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormState
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.coroutines.flow.StateFlow

@Suppress("TooManyFunctions")
internal interface TransactionFormComponentInternal : TransactionFormComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>
    val tagModal: Value<ChildSlot<*, TagComponent>>
    val currencyModal: Value<ChildSlot<*, CurrencyComponent>>

    val state: StateFlow<FormState>

    fun openCategoryModal()
    fun closeCategoryModal()
    fun setCategory(category: CategoryEntity)

    fun openTagModal()
    fun closeTagModal()
    fun addTag(tag: TagEntity)
    fun removeTag(tag: TagEntity)

    fun openCurrencyModal()
    fun closeCurrencyModal()
    fun selectCurrency(currency: CurrencyEntity)

    fun onBackClicked()

    fun setAmount(amount: String)
    fun onDateSelected(timestamp: Long?)

    fun onSubmitClicked()
}
