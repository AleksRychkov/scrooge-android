package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentation.component.calculator.CalculatorComponent
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.tags.TagComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Suppress("TooManyFunctions")
internal interface TransactionFormComponentInternal : TransactionFormComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>
    val tagModal: Value<ChildSlot<*, TagComponent>>
    val currencyModal: Value<ChildSlot<*, CurrencyComponent>>
    val calculatorModal: Value<ChildSlot<*, CalculatorComponent>>

    val state: StateFlow<FormState>
    val effects: Flow<FormEffect>

    fun submit()
    fun delete()
    fun appendInput(input: String)
    fun removeLastFromInput()
    fun updateComment(comment: String)
    fun updateDate(timestamp: Long?)

    fun openCurrencyModal()
    fun closeCurrencyModal()
    fun selectCurrency(currency: CurrencyEntity)

    fun setAmount(amount: String)

    fun openCategoryModal()
    fun closeCategoryModal()
    fun setCategory(category: CategoryEntity)

    fun openTagModal()
    fun closeTagModal()
    fun setTags(tags: Set<TagEntity>)

    fun openCalculatorModal()
    fun closeCalculatorModal()
}
