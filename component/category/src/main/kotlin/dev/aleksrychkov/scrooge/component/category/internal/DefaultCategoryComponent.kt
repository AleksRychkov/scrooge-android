package dev.aleksrychkov.scrooge.component.category.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryActor
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryReducer
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryState
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow

internal class DefaultCategoryComponent(
    private val componentContext: ComponentContext,
    private val transactionType: TransactionType,
) : CategoryComponentInternal, ComponentContext by componentContext {

    private val store: Store<CategoryState, CategoryEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = CategoryState(),
            actor = CategoryActor(),
            reducer = CategoryReducer(),
            startEvent = CategoryEvent.External.Init(transactionType),
        )
    }

    override val state: StateFlow<CategoryState>
        get() = store.state

    override fun deleteCategory(categoryEntity: CategoryEntity) {
        store.handle(CategoryEvent.External.Delete(categoryId = categoryEntity.id))
    }

    override fun setSearchQuery(query: String) {
        store.handle(CategoryEvent.External.Search(query = query))
    }
}
