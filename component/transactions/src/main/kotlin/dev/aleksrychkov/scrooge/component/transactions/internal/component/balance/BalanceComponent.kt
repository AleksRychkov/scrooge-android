package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceActor
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceEvent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceReducer
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceState
import dev.aleksrychkov.scrooge.component.transactions.internal.utils.DateTimeUtils
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionsList
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Instant

internal interface BalanceComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
        ): BalanceComponent {
            return DefaultBalanceComponent(componentContext = componentContext)
        }
    }

    val state: StateFlow<BalanceState>

    fun setPeriod(instant: Instant)
    fun onDetailsClicked()
}

private class DefaultBalanceComponent(
    componentContext: ComponentContext
) : BalanceComponent, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val store: Store<BalanceState, BalanceEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = BalanceState(isLoading = true),
            actor = BalanceActor(),
            reducer = BalanceReducer(),
        )
    }

    override val state: StateFlow<BalanceState>
        get() = store.state

    override fun setPeriod(instant: Instant) {
        store.handle(BalanceEvent.External.SetPeriod(instant = instant))
    }

    override fun onDetailsClicked() {
        val period = state.value.period
        val startEnd = DateTimeUtils.getMonthStartEndTimestamp(period)
        DestinationTransactionsList(
            periodFrom = startEnd.first,
            periodTo = startEnd.second,
        ).let(router::open)
    }
}
