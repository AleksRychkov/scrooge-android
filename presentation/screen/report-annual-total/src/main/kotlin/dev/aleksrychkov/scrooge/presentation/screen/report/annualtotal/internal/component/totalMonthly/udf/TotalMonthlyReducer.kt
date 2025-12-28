package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal class TotalMonthlyReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<TotalMonthlyState, TotalMonthlyEvent, TotalMonthlyCommand, Unit> {
    override fun reduce(
        event: TotalMonthlyEvent,
        state: TotalMonthlyState
    ): ReducerResult<TotalMonthlyState, TotalMonthlyCommand, Unit> {
        return when (event) {
            is TotalMonthlyEvent.External.Load -> state.reduceWith(event) {
                val year = Instant
                    .fromEpochMilliseconds(event.filter.period.to)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .year
                state {
                    copy(isLoading = true, filter = event.filter, currentYear = year)
                }
                command {
                    listOf(TotalMonthlyCommand.Load(filter = filter))
                }
            }

            TotalMonthlyEvent.Internal.FailedToLoad -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
            }

            is TotalMonthlyEvent.Internal.Success -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        byMonth = event.res.mapToState(
                            resourceManager = resourceManager,
                            year = state.currentYear,
                        ),
                    )
                }
            }
        }
    }
}
