package dev.aleksrychkov.scrooge.presentation.component.limits.internal

import com.arkivanov.decompose.ComponentContext
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

    private val store: Store<LimitsState, LimitsEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = LimitsState(),
            actor = LimitsActor(),
            reducer = LimitsReducer(),
            startEvent = LimitsEvent.External.Init,
        )
    }

    override val state: StateFlow<LimitsState>
        get() = store.state
}
