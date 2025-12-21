package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class PeriodTotalReducer :
    Reducer<PeriodTotalState, PeriodTotalEvent, PeriodTotalCommand, Unit> {
    override fun reduce(
        event: PeriodTotalEvent,
        state: PeriodTotalState
    ): ReducerResult<PeriodTotalState, PeriodTotalCommand, Unit> {
        return when (event) {
            is PeriodTotalEvent.External.Load -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(
                        PeriodTotalCommand.Load(
                            fromTimestamp = event.fromTimestamp,
                            toTimestamp = event.toTimestamp,
                        )
                    )
                }
            }

            PeriodTotalEvent.Internal.LoadFailed -> state.reduceWith(event) {
                state {
                    copy(isLoading = false, data = PeriodTotalState.ByType())
                }
            }

            is PeriodTotalEvent.Internal.LoadSuccess -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        data = event.result.mapToStateValue(),
                    )
                }
            }
        }
    }
}
