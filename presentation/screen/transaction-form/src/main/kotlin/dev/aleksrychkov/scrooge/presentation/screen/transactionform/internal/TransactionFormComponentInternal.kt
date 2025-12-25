package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.TagComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Suppress("TooManyFunctions")
internal interface TransactionFormComponentInternal : TransactionFormComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>
    val tagModal: Value<ChildSlot<*, TagComponent>>
    val currencyModal: Value<ChildSlot<*, CurrencyComponent>>

    val state: StateFlow<FormState>
    val effects: Flow<FormEffect>

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
    fun onDeleteClicked()
}
