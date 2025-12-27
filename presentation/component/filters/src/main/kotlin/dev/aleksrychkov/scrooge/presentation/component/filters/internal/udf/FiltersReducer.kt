package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.startEndOfMonth
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.component.filters.FilterEntityFactory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class FiltersReducer(
    private val resourceManager: Lazy<ResourceManager> = getLazy()
) : Reducer<FiltersState, FiltersEvent, FiltersCommand, Unit> {

    @Suppress("LongMethod")
    override fun reduce(
        event: FiltersEvent,
        state: FiltersState
    ): ReducerResult<FiltersState, FiltersCommand, Unit> {
        return when (event) {
            is FiltersEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(FiltersCommand.GetFiltersStartEndYears)
                }
                state {
                    val period = event.filter.period
                    val startDate = period.from.timestampToDate()
                    val endDate = period.to.timestampToDate()
                    val selectedYear = if (startDate.year == endDate.year) {
                        startDate.year
                    } else {
                        -1
                    }
                    val selectedMonth =
                        if (startDate.year == endDate.year && startDate.month == endDate.month) {
                            startDate.month.number
                        } else {
                            -1
                        }
                    copy(
                        filter = event.filter,
                        filterReadable = event.filter.readableName,
                        selectedYear = selectedYear,
                        selectedMonthNumber = selectedMonth,
                    )
                }
            }

            is FiltersEvent.Internal.SetMinMaxYearsPeriod -> state.reduceWith(event) {
                state {
                    copy(
                        allYears = (event.startYear..event.endYear).toImmutableList(),
                        allMonths = resourceManager.value
                            .getStringArray(Resources.array.month_names)
                            .toImmutableList(),
                    )
                }
            }

            is FiltersEvent.External.MonthClicked -> state.reduceWith(event) {
                val period = startEndOfMonth(month = Month(event.month), state.selectedYear)
                val filter = FilterEntityFactory.fromPeriod(period, resourceManager.value)
                state {
                    copy(
                        selectedMonthNumber = event.month,
                        filter = filter,
                        filterReadable = filter.readableName,
                    )
                }
            }

            is FiltersEvent.External.YearClicked -> state.reduceWith(event) {
                val period = startEndOfMonth(month = Month(state.selectedMonthNumber), event.year)
                val filter = FilterEntityFactory.fromPeriod(period, resourceManager.value)
                state {
                    copy(
                        selectedYear = event.year,
                        filter = filter,
                        filterReadable = filter.readableName,
                    )
                }
            }
        }
    }
}

private fun Long.timestampToDate(tz: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(tz)
        .date
