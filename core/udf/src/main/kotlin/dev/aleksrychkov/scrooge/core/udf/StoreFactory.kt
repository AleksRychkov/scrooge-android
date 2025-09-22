package dev.aleksrychkov.scrooge.core.udf

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

fun <State : Any, Event : Any, Command : Any, Effect : Any> udfStore(
    storeScope: CoroutineScope = UdfScope(),
    storeDispatcher: CoroutineDispatcher = UdfDispatcher,
    initialState: State,
    actor: Actor<Command, Event>,
    reducer: Reducer<State, Event, Command, Effect>,
): Store<State, Event, Effect> = UdfStore(
    storeScope = storeScope,
    storeDispatcher = storeDispatcher,
    initialState = initialState,
    actor = actor,
    reducer = reducer,
)
