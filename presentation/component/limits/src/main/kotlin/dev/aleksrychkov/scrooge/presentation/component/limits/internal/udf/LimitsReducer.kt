package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class LimitsReducer : Reducer<LimitsState, LimitsEvent, LimitsCommand, Unit> {
    override fun reduce(
        event: LimitsEvent,
        state: LimitsState
    ): ReducerResult<LimitsState, LimitsCommand, Unit> {
        return when (event) {

            is LimitsEvent.Internal.LimitsResult -> state.reduceWith(event) {
                println(event.limits.joinToString())
            }

            LimitsEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(LimitsCommand.ObserveLimits)
                }
            }
        }
    }
}
