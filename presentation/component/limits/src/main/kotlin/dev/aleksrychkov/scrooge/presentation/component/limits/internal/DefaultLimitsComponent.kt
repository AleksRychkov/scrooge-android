package dev.aleksrychkov.scrooge.presentation.component.limits.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.router.DestinationLimits
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsActor
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsEvent
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsReducer
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultLimitsComponent(
    componentContext: ComponentContext,
) : LimitsComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val store: Store<LimitsState, LimitsEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = LimitsState(),
            actor = LimitsActor(),
            reducer = LimitsReducer(),
        )
    }

    override val state: StateFlow<LimitsState>
        get() = store.state

    override fun setFilter(filter: FilterEntity) {
        store.handle(LimitsEvent.External.Load(filter = filter))
    }

    override fun navigateToLimitSettings() {
        router.open(DestinationLimits)
    }
}
