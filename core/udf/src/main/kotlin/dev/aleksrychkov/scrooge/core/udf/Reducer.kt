package dev.aleksrychkov.scrooge.core.udf

interface Reducer<State : Any, Event : Any, Command : Any, Effect : Any> {
    fun reduce(event: Event, state: State): ReducerResult<State, Command, Effect>
}

data class ReducerResult<State : Any, Command : Any, Effect : Any>(
    val state: State,
    val commands: List<Command> = emptyList(),
    val effects: List<Effect> = emptyList(),
)

class ReducerResultBuilder<State : Any, Event : Any, Command : Any, Effect : Any>(
    private var state: State,
    private val event: Event,
) {
    private var commands: List<Command> = emptyList()
    private var effects: List<Effect> = emptyList()

    fun state(block: State.(Event) -> State) {
        state = block(state, event)
    }

    fun command(block: State.(Event) -> List<Command>) {
        commands = block(state, event)
    }

    fun effects(block: State.(Event) -> List<Effect>) {
        effects = block(state, event)
    }

    fun build() = ReducerResult(state, commands, effects)
}

inline fun <State : Any, Event : Any, Command : Any, Effect : Any> State.reduceWith(
    event: Event,
    block: ReducerResultBuilder<State, Event, Command, Effect>.() -> Unit
): ReducerResult<State, Command, Effect> =
    ReducerResultBuilder<State, Event, Command, Effect>(this, event).apply(block).build()
