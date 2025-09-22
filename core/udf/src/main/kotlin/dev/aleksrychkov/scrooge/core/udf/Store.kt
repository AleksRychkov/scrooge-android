package dev.aleksrychkov.scrooge.core.udf

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Store<State : Any, Event : Any, Effect : Any> {
    val state: StateFlow<State>
    val effects: Flow<Effect>
    val storeScope: CoroutineScope
    val storeDispatcher: CoroutineDispatcher

    fun handle(event: Event)
    fun stop()
}
