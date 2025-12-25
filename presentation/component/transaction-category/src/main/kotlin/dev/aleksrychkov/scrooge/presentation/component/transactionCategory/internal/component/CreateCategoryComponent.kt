package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryActor
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryEvent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryReducer
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface CreateCategoryComponent {
    companion object {
        operator fun invoke(
            name: String,
            transactionType: TransactionType,
            componentContext: ComponentContext,
        ): CreateCategoryComponent {
            return DefaultCreateCategoryComponent(
                componentContext = componentContext,
                name = name,
                transactionType = transactionType,
            )
        }
    }

    val state: StateFlow<CreateCategoryState>
    val effects: Flow<CreateCategoryEffect>

    fun setName(name: String)
    fun setIcon(icon: CategoryIcon)
    fun setColor(color: Int)
    fun submit()
}

private class DefaultCreateCategoryComponent(
    componentContext: ComponentContext,
    private val name: String,
    private val transactionType: TransactionType,
) : CreateCategoryComponent, ComponentContext by componentContext {

    private val store: Store<CreateCategoryState, CreateCategoryEvent, CreateCategoryEffect> by lazy {
        instanceKeeper.createStore(
            initialState = CreateCategoryState(
                name = name,
                transactionType = transactionType,
            ),
            actor = CreateCategoryActor(),
            reducer = CreateCategoryReducer(),
            startEvent = CreateCategoryEvent.External.Init,
        )
    }

    override val state: StateFlow<CreateCategoryState>
        get() = store.state

    override val effects: Flow<CreateCategoryEffect>
        get() = store.effects

    override fun setName(name: String) {
        store.handle(CreateCategoryEvent.External.SetName(name))
    }

    override fun setIcon(icon: CategoryIcon) {
        store.handle(CreateCategoryEvent.External.SetIcon(icon))
    }

    override fun submit() {
        store.handle(CreateCategoryEvent.External.Submit)
    }

    override fun setColor(color: Int) {
        store.handle(CreateCategoryEvent.External.SetColor(color))
    }
}
