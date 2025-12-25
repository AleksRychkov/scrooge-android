package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.CreateCategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryActor
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryReducer
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal class DefaultCategoryComponent(
    private val componentContext: ComponentContext,
    private val transactionType: TransactionType,
) : CategoryComponentInternal, ComponentContext by componentContext {

    private val createCategoryNavigation = SlotNavigation<Pair<String, TransactionType>>()

    private val store: Store<CategoryState, CategoryEvent, CategoryEffect> by lazy {
        instanceKeeper.createStore(
            initialState = CategoryState(),
            actor = CategoryActor(),
            reducer = CategoryReducer(),
            startEvent = CategoryEvent.External.Init(transactionType),
        )
    }

    override val createCategoryModal: Value<ChildSlot<*, CreateCategoryComponent>> =
        childSlot(
            source = createCategoryNavigation,
            serializer = null,
            handleBackButton = true,
            key = "CreateCategoryModalSlot",
        ) { pair, childComponentContext ->
            CreateCategoryComponent.Companion(
                componentContext = childComponentContext,
                name = pair.first,
                transactionType = pair.second,
            )
        }

    override val state: StateFlow<CategoryState>
        get() = store.state

    override val effects: Flow<CategoryEffect>
        get() = store.effects

    override fun deleteCategory(category: CategoryEntity) {
        store.handle(CategoryEvent.External.Delete(category = category))
    }

    override fun restoreCategory(category: CategoryEntity) {
        store.handle(CategoryEvent.External.Restore(category = category))
    }

    override fun setSearchQuery(query: String) {
        store.handle(CategoryEvent.External.Search(query = query))
    }

    override fun openAddCategoryModal() {
        val categoryNameToAdd = state.value.searchQuery
        val type = state.value.transactionType
        createCategoryNavigation.activate(categoryNameToAdd to type)
    }

    override fun closeAddCategoryModal() {
        createCategoryNavigation.dismiss()
    }
}
