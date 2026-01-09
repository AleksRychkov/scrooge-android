package dev.aleksrychkov.scrooge.presentation.component.category.internal

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
import dev.aleksrychkov.scrooge.presentation.component.category.internal.component.CreateCategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryActor
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryReducer
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal class DefaultCategoryComponent(
    private val componentContext: ComponentContext,
    transactionType: TransactionType?,
    isEditable: Boolean,
) : CategoryComponentInternal, ComponentContext by componentContext {

    private val createCategoryNavigation = SlotNavigation<CreateCategoryDto>()

    private val store: Store<CategoryState, CategoryEvent, CategoryEffect> by lazy {
        instanceKeeper.createStore(
            initialState = CategoryState(
                isEditable = isEditable,
                transactionType = transactionType
            ),
            actor = CategoryActor(),
            reducer = CategoryReducer(),
            startEvent = CategoryEvent.External.Init,
        )
    }

    override val createCategoryModal: Value<ChildSlot<*, CreateCategoryComponent>> =
        childSlot(
            source = createCategoryNavigation,
            serializer = null,
            handleBackButton = true,
            key = "CreateCategoryModalSlot",
        ) { dto, childComponentContext ->
            CreateCategoryComponent.Companion(
                componentContext = childComponentContext,
                name = dto.name,
                transactionType = dto.type,
                id = dto.id,
            )
        }

    override val state: StateFlow<CategoryState>
        get() = store.state

    override val effects: Flow<CategoryEffect>
        get() = store.effects

    override fun deleteCategory(category: CategoryEntity) {
        store.handle(CategoryEvent.External.Delete(category = category))
    }

    override fun editCategory(category: CategoryEntity) {
        createCategoryNavigation.activate(
            CreateCategoryDto(
                name = category.name,
                type = category.type,
                id = category.id,
            )
        )
    }

    override fun setSearchQuery(query: String) {
        store.handle(CategoryEvent.External.Search(query = query))
    }

    override fun openAddCategoryModal() {
        val categoryNameToAdd = state.value.searchQuery
        val type = state.value.transactionType ?: return
        createCategoryNavigation.activate(CreateCategoryDto(name = categoryNameToAdd, type = type))
    }

    override fun closeAddCategoryModal() {
        createCategoryNavigation.dismiss()
    }

    override fun swapOrder(newOrder: List<Pair<Long, Int>>) {
        store.handle(CategoryEvent.External.SwapOrder(newOrder = newOrder))
    }

    private class CreateCategoryDto(
        val name: String,
        val type: TransactionType,
        val id: Long? = null
    )
}
