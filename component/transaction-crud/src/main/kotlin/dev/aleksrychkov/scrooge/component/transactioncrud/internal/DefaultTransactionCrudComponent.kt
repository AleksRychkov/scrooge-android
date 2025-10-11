package dev.aleksrychkov.scrooge.component.transactioncrud.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

@Suppress("UnusedPrivateProperty")
internal class DefaultTransactionCrudComponent(
    private val componentContext: ComponentContext,
    private val transactionId: Long? = null,
    private val transactionType: TransactionType,
) : TransactionCrudComponentInternal, ComponentContext by componentContext {

    private val categoryNavigation = SlotNavigation<TransactionType>()

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

    override fun openCategoryModal() {
        categoryNavigation.activate(transactionType)
    }

    override fun closeCategoryModal() {
        categoryNavigation.dismiss()
    }

    override fun setCategory(value: CategoryEntity) {
        println("ASDASD $value")
    }
}
