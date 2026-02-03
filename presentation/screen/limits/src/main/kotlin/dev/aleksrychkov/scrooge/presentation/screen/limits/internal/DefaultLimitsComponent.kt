package dev.aleksrychkov.scrooge.presentation.screen.limits.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentaion.component.currency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsActor
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsReducer
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultLimitsComponent(
    componentContext: ComponentContext
) : LimitsComponentInternal, ComponentContext by componentContext {

    private val currencyNavigation = SlotNavigation<Long>()

    private val store: Store<LimitsState, LimitsEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = LimitsState(),
            actor = LimitsActor(),
            reducer = LimitsReducer(),
            startEvent = LimitsEvent.External.Init,
        )
    }

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    override val state: StateFlow<LimitsState>
        get() = store.state

    override val currencyModal: Value<ChildSlot<*, CurrencySlotDto>> =
        childSlot(
            source = currencyNavigation,
            serializer = null,
            handleBackButton = true,
            key = "CurrencyModalSlot",
        ) { id, childComponentContext ->
            CurrencySlotDto(
                component = CurrencyComponent(componentContext = childComponentContext),
                limitId = id
            )
        }

    override fun onBackPressed() {
        router.close()
    }

    override fun onAddLimitClicked() {
        store.handle(LimitsEvent.External.AddNewLimit)
    }

    override fun onDeleteLimitClicked(id: Long) {
        store.handle(LimitsEvent.External.DeleteLimit(id = id))
    }

    override fun onAmountChanged(id: Long, value: String) {
        store.handle(LimitsEvent.External.AmountChanged(id = id, value = value))
    }

    override fun onPeriodChanged(id: Long, period: String) {
        store.handle(LimitsEvent.External.PeriodChanged(id = id, value = period))
    }

    override fun openCurrencyModal(id: Long) {
        currencyNavigation.activate(id)
    }

    override fun closeCurrencyModal() {
        currencyNavigation.dismiss()
    }

    override fun selectCurrency(
        id: Long,
        currency: CurrencyEntity,
    ) {
        store.handle(LimitsEvent.External.CurrencyChanged(id = id, value = currency))
    }
}
