package dev.aleksrychkov.scrooge.component.currency.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyActor
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyEffect
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyEvent
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyReducer
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyState
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow

internal class DefaultCurrencyComponent(
    private val componentContext: ComponentContext,
) : CurrencyComponentInternal, ComponentContext by componentContext {

    private val store: Store<CurrencyState, CurrencyEvent, CurrencyEffect> by lazy {
        instanceKeeper.createStore(
            initialState = CurrencyState(),
            actor = CurrencyActor(),
            reducer = CurrencyReducer(),
            startEvent = CurrencyEvent.External.Init,
        )
    }

    override val state: StateFlow<CurrencyState>
        get() = store.state

    override fun setSearchQuery(query: String) {
        store.handle(CurrencyEvent.External.Search(query = query))
    }
}
