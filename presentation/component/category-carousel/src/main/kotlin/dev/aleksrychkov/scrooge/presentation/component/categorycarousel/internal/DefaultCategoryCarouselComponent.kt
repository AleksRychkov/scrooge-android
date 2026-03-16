package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal

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
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CarouselItem
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselActor
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselEvent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselReducer
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultCategoryCarouselComponent(
    componentContext: ComponentContext,
    private val type: TransactionType,
    callback: (CategoryEntity) -> Unit,
) : CategoryCarouselComponentInternal, ComponentContext by componentContext {
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
    private val store: Store<CategoryCarouselState, CategoryCarouselEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = CategoryCarouselState(),
            actor = CategoryCarouselActor(),
            reducer = CategoryCarouselReducer(callback = callback), // todo: don't like it
            startEvent = CategoryCarouselEvent.External.ObserveCategories(type = type),
        )
    }

    override val state: StateFlow<CategoryCarouselState>
        get() = store.state

    override fun selectItem(item: CarouselItem) {
        store.handle(CategoryCarouselEvent.External.SelectCategory(category = item.ref))
    }

    // region Category
    override fun openCategoryModal() {
        categoryNavigation.activate(type)
    }

    override fun closeCategoryModal() {
        categoryNavigation.dismiss()
    }

    override fun setCategory(category: CategoryEntity) {
        if (state.value.selectedCategory == category) return
        store.handle(CategoryCarouselEvent.External.SelectCategory(category = category))
    }
    // endregion Category
}
