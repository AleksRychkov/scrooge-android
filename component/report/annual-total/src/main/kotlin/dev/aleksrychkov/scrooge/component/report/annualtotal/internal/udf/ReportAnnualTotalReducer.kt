package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class ReportAnnualTotalReducer :
    Reducer<ReportAnnualTotalState, ReportAnnualTotalEvent, ReportAnnualTotalCommand, Unit> {
    override fun reduce(
        event: ReportAnnualTotalEvent,
        state: ReportAnnualTotalState
    ): ReducerResult<ReportAnnualTotalState, ReportAnnualTotalCommand, Unit> =
        when (event) {
            is ReportAnnualTotalEvent.External.Initial -> state.reduceWith(event) {
                state {
                    copy(isLoading = true, selectedYear = event.year)
                }
                command {
                    listOf(ReportAnnualTotalCommand.Load(year = event.year))
                }
            }

            ReportAnnualTotalEvent.External.DecrementYear -> state.reduceWith(event) {
                val year = state.selectedYear - 1
                state {
                    copy(isLoading = true, selectedYear = year)
                }
                command {
                    listOf(ReportAnnualTotalCommand.Load(year = year))
                }
            }

            ReportAnnualTotalEvent.External.IncrementYear -> state.reduceWith(event) {
                val year = state.selectedYear + 1
                state {
                    copy(isLoading = true, selectedYear = year)
                }
                command {
                    listOf(ReportAnnualTotalCommand.Load(year = year))
                }
            }
        }
}
