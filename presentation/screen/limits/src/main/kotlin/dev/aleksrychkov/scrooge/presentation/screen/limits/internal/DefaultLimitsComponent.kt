package dev.aleksrychkov.scrooge.presentation.screen.limits.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsActor
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsReducer
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultLimitsComponent(
    componentContext: ComponentContext
) : LimitsComponentInternal, ComponentContext by componentContext {

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

    override fun onCurrencyChanged(id: Long, code: String) {
        TODO("Not yet implemented")
    }

    override fun onPeriodChanged(id: Long, period: String) {
        TODO("Not yet implemented")
    }
}
