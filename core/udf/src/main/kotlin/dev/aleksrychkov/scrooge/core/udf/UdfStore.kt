package dev.aleksrychkov.scrooge.core.udf

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

open class UdfStore<State : Any, Event : Any, Effect : Any, Command : Any>(
    final override val storeScope: CoroutineScope,
    final override val storeDispatcher: CoroutineDispatcher,
    initialState: State,
    private val actor: Actor<Command, Event>,
    private val reducer: Reducer<State, Event, Command, Effect>,
) : Store<State, Event, Effect> {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state.asStateFlow()

    private val _effects: MutableSharedFlow<Effect> = MutableSharedFlow(
        extraBufferCapacity = Int.MAX_VALUE,
    )
    override val effects: Flow<Effect> = _effects.asSharedFlow()

    private val reducerDispatcher: CoroutineDispatcher = storeDispatcher.limitedParallelism(1)

    override fun handle(event: Event) {
        reduce(event)
    }

    override fun stop() {
        storeScope.cancel()
    }

    @Suppress("TooGenericExceptionCaught", "RethrowCaughtException")
    private fun reduce(event: Event) {
        storeScope.launch(reducerDispatcher) {
            try {
                val result = reducer.reduce(event, _state.value)
                _state.value = result.state
                result.effects.forEach { handleEffect(it) }
                result.commands.forEach { handleCommand(it) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // todo: proper handling
                throw e
            }
        }
    }

    private suspend fun handleEffect(effect: Effect) {
        if (!coroutineContext.isActive) return
        _effects.emit(effect)
    }

    private suspend fun handleCommand(command: Command) {
        if (!coroutineContext.isActive) return
        storeScope.launch {
            actor
                .process(command)
                .cancellable()
                .catch { t ->
                    // todo: logging
                }
                .collect { handle(it) }
        }
    }
}
