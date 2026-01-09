package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.tags.TagComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormActor
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormReducer
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Suppress("UnusedPrivateProperty", "TooManyFunctions")
internal class DefaultTransactionFormComponent(
    private val componentContext: ComponentContext,
    private val transactionId: Long? = null,
    private val transactionType: TransactionType,
) : TransactionFormComponentInternal, ComponentContext by componentContext {

    private val categoryNavigation = SlotNavigation<TransactionType>()
    private val tagNavigation = SlotNavigation<Unit>()
    private val currencyNavigation = SlotNavigation<Unit>()

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val store: Store<FormState, FormEvent, FormEffect> by lazy {
        instanceKeeper.createStore(
            initialState = FormState(
                transactionType = transactionType,
                transactionId = transactionId,
            ),
            actor = FormActor.Companion(router = router),
            reducer = FormReducer(resourceManager = get()),
            startEvent = FormEvent.External.Init(transactionId),
        )
    }

    override val state: StateFlow<FormState>
        get() = store.state

    override val effects: Flow<FormEffect>
        get() = store.effects

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
        ) { _, childComponentContext ->
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
        ) { _, childComponentContext ->
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

    override fun setCategory(category: CategoryEntity) {
        store.handle(FormEvent.External.SetCategory(category = category))
    }

    override fun openTagModal() {
        tagNavigation.activate(Unit)
    }

    override fun closeTagModal() {
        tagNavigation.dismiss()
    }

    override fun addTag(tag: TagEntity) {
        store.handle(FormEvent.External.AddTag(tag = tag))
    }

    override fun removeTag(tag: TagEntity) {
        store.handle(FormEvent.External.RemoveTag(tag = tag))
    }

    override fun openCurrencyModal() {
        currencyNavigation.activate(Unit)
    }

    override fun closeCurrencyModal() {
        currencyNavigation.dismiss()
    }

    override fun selectCurrency(currency: CurrencyEntity) {
        store.handle(FormEvent.External.SetCurrency(currency = currency))
    }

    override fun onBackClicked() {
        router.close()
    }

    override fun setAmount(amount: String) {
        store.handle(FormEvent.External.SetAmount(amount = amount))
    }

    override fun onSubmitClicked() {
        store.handle(FormEvent.External.Submit)
    }

    override fun onDeleteClicked() {
        store.handle(FormEvent.External.Delete)
    }

    override fun onDateSelected(timestamp: Long?) {
        if (timestamp == null) return
        store.handle(FormEvent.External.SetDate(timestamp = timestamp))
    }
}
