package dev.aleksrychkov.scrooge.core.udfextensions

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udf.UdfDispatcher
import dev.aleksrychkov.scrooge.core.udf.UdfScope
import dev.aleksrychkov.scrooge.core.udf.UdfStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class RetainedStore<State : Any, Event : Any, Effect : Any, Command : Any>(
    storeScope: CoroutineScope,
    storeDispatcher: CoroutineDispatcher,
    initialState: State,
    actor: Actor<Command, Event>,
    reducer: Reducer<State, Event, Command, Effect>,
) : UdfStore<State, Event, Effect, Command>(
    storeScope,
    storeDispatcher,
    initialState,
    actor,
    reducer
),
    InstanceKeeper.Instance {
    override fun onDestroy() {
        stop()
    }
}

fun <State : Any, Event : Any, Command : Any, Effect : Any> InstanceKeeper.createStore(
    storeScope: CoroutineScope = UdfScope(),
    storeDispatcher: CoroutineDispatcher = UdfDispatcher,
    initialState: State,
    actor: Actor<Command, Event>,
    reducer: Reducer<State, Event, Command, Effect>,
    startEvent: Event? = null,
): Store<State, Event, Effect> {
    return this.getOrCreate(key = initialState::class.java.name) {
        RetainedStore(
            storeScope = storeScope,
            storeDispatcher = storeDispatcher,
            initialState = initialState,
            actor = actor,
            reducer = reducer,
        ).also {
            if (startEvent != null) {
                it.handle(startEvent)
            }
        }
    }
}
