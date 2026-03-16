package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentation.component.calculator.CalculatorComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselComponent
import dev.aleksrychkov.scrooge.presentation.component.tags.TagComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormActor
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormReducer
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Suppress("TooManyFunctions")
internal class DefaultTransactionFormComponent(
    componentContext: ComponentContext,
    private val transactionId: Long?,
    override val transactionType: TransactionType,
) : TransactionFormComponentInternal, ComponentContext by componentContext {

    private val tagNavigation = SlotNavigation<Unit>()
    private val currencyNavigation = SlotNavigation<Unit>()
    private val calculatorNavigation = SlotNavigation<Unit>()

    private val _categoryCarouselComponent: CategoryCarouselComponent by lazy {
        CategoryCarouselComponent(
            componentContext = childContext("TransactionFormComponentCategoryCarousel"),
            type = transactionType,
        ) { category ->
            store.handle(FormEvent.External.SetCategory(category = category))
        }
    }

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val store: Store<FormState, FormEvent, FormEffect> by lazy {
        instanceKeeper.createStore(
            initialState = FormState(
                transactionType = transactionType,
                transactionId = transactionId,
            ),
            actor = FormActor(router = router),
            reducer = FormReducer(resourceManager = get()),
            startEvent = FormEvent.External.Init(transactionId),
        )
    }

    override val state: StateFlow<FormState>
        get() = store.state

    override val effects: Flow<FormEffect>
        get() = store.effects

    override val categoryCarouselComponent: CategoryCarouselComponent
        get() = _categoryCarouselComponent

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

    override val calculatorModal: Value<ChildSlot<*, CalculatorComponent>> =
        childSlot(
            source = calculatorNavigation,
            serializer = null,
            handleBackButton = true,
            key = "CalculatorModalSlot",
        ) { _, childComponentContext ->
            CalculatorComponent(
                componentContext = childComponentContext,
            )
        }

    // region Inputs
    override fun submit() {
        store.handle(FormEvent.External.Submit)
    }

    override fun delete() {
        store.handle(FormEvent.External.Delete)
    }

    // region Comment
    override fun updateComment(comment: String) {
        store.handle(FormEvent.External.SetComment(comment = comment))
    }

    override fun updateDate(timestamp: Long?) {
        if (timestamp == null) return
        store.handle(FormEvent.External.SetDate(timestamp = timestamp))
    }
    // endregion Comment

    // region Amount
    override fun appendInput(input: String) {
        store.handle(FormEvent.External.AppendAmount(value = input))
    }

    override fun removeLastFromInput() {
        store.handle(FormEvent.External.RemoveLastFromAmount)
    }

    override fun setAmount(amount: String) {
        store.handle(FormEvent.External.SetAmount(amount = amount))
    }
    // endregion Amount

    // region Currency
    override fun openCurrencyModal() {
        currencyNavigation.activate(Unit)
    }

    override fun closeCurrencyModal() {
        currencyNavigation.dismiss()
    }

    override fun selectCurrency(currency: CurrencyEntity) {
        store.handle(FormEvent.External.SetCurrency(currency = currency))
    }
    // endregion Currency

    // region Calculator
    override fun openCalculatorModal() {
        calculatorNavigation.activate(Unit)
    }

    override fun closeCalculatorModal() {
        calculatorNavigation.dismiss()
    }
    // endregion Calculator

    // region Tag
    override fun openTagModal() {
        tagNavigation.activate(Unit)
    }

    override fun closeTagModal() {
        tagNavigation.dismiss()
    }

    override fun setTags(tags: Set<TagEntity>) {
        store.handle(FormEvent.External.SetTags(tags = tags))
    }
    // endregion Tag
    // endregion Inputs
}
