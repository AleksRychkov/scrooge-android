package dev.aleksrychkov.scrooge.component.transactionform.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.component.tag.TagComponent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

@Suppress("UnusedPrivateProperty")
internal class DefaultTransactionFormComponent(
    private val componentContext: ComponentContext,
    private val transactionId: Long? = null,
    private val transactionType: TransactionType,
) : TransactionFormComponentInternal, ComponentContext by componentContext {

    private val categoryNavigation = SlotNavigation<TransactionType>()
    private val tagNavigation = SlotNavigation<Unit>()
    private val currencyNavigation = SlotNavigation<Unit>()

    override val categoryModal: Value<ChildSlot<*, CategoryComponent>> =
        childSlot(
            source = categoryNavigation,
            serializer = null,
            handleBackButton = true,
            key = "CategoryModalSlot",
        ) { type, childComponentContext ->
            CategoryComponent(
                componentContext = childComponentContext,
                transactionType = type,
            )
        }

    override val tagModal: Value<ChildSlot<*, TagComponent>> =
        childSlot(
            source = tagNavigation,
            serializer = null,
            handleBackButton = true,
            key = "TagModalSlot",
        ) { type, childComponentContext ->
            TagComponent(
                componentContext = childComponentContext,
            )
        }

    override val currencyModal: Value<ChildSlot<*, CurrencyComponent>> =
        childSlot(
            source = currencyNavigation,
            serializer = null,
            handleBackButton = true,
            key = "CurrencyModalSlot",
        ) { type, childComponentContext ->
            CurrencyComponent(
                componentContext = childComponentContext,
            )
        }

    override fun openCategoryModal() {
        categoryNavigation.activate(transactionType)
    }

    override fun closeCategoryModal() {
        categoryNavigation.dismiss()
    }

    override fun setCategory(value: CategoryEntity) {
        println("ASDASD set category $value")
    }

    override fun openTagModal() {
        tagNavigation.activate(Unit)
    }

    override fun closeTagModal() {
        tagNavigation.dismiss()
    }

    override fun addTag(value: TagEntity) {
        println("ASDASD add tag $value")
    }

    override fun removeTag(value: TagEntity) {
        println("ASDASD remove tag $value")
    }

    override fun openCurrencyModal() {
        currencyNavigation.activate(Unit)
    }

    override fun closeCurrencyModal() {
        currencyNavigation.dismiss()
    }

    override fun selectCurrency(value: CurrencyEntity) {
        println("ASDASD selectCurrency $value")
    }
}
