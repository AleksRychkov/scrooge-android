package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class RootTransferReducer :
    Reducer<RootTransferState, RootTransferEvent, RootTransferCommand, Unit> {
    override fun reduce(
        event: RootTransferEvent,
        state: RootTransferState
    ): ReducerResult<RootTransferState, RootTransferCommand, Unit> {
        return when (event) {
            RootTransferEvent.External.ObserveState -> state.reduceWith(event) {
                command {
                    listOf(RootTransferCommand.ObserveState)
                }
            }

            is RootTransferEvent.Internal.ObserveResult -> state.reduceWith(event) {
                state {
                    copy(state = event.state)
                }
            }

            is RootTransferEvent.External.SetState -> state.reduceWith(event) {
                command {
                    listOf(RootTransferCommand.SetState(state = event.state))
                }
            }
        }
    }
}
