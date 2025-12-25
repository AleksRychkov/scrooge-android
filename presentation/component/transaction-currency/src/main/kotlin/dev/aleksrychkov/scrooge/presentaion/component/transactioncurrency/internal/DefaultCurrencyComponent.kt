package dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyActor
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyEffect
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyEvent
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyReducer
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf.CurrencyState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultCurrencyComponent(
    private val componentContext: ComponentContext,
) : CurrencyComponentInternal, ComponentContext by componentContext {

    private val store: Store<CurrencyState, CurrencyEvent, CurrencyEffect> by lazy {
        instanceKeeper.createStore(
            initialState = CurrencyState(),
            actor = CurrencyActor.Companion(),
            reducer = CurrencyReducer(),
            startEvent = CurrencyEvent.External.Init,
        )
    }

    override val state: StateFlow<CurrencyState>
        get() = store.state

    override fun removeFromFavorite(currency: CurrencyEntity) {
        store.handle(CurrencyEvent.External.RemoveFromFavorite(currency = currency))
    }

    override fun addToFavorite(currency: CurrencyEntity) {
        store.handle(CurrencyEvent.External.AddToFavorite(currency = currency))
    }
}
