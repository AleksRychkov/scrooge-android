package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.component.filters.FilterEntityFactory
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.daysInMonth
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
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
                    val tz = TimeZone.currentSystemDefault()
                    val period = event.filter.period
                    val startDate = Instant
                        .fromEpochMilliseconds(period.from)
                        .toLocalDateTime(tz)
                        .date
                    val endDate = Instant
                        .fromEpochMilliseconds(period.to)
                        .toLocalDateTime(tz)
                        .date
                    val endSelection = if (startDate == endDate) {
                        null
                    } else {
                        FiltersState.SelectionDate(
                            year = endDate.year,
                            month = endDate.month.number,
                            day = endDate.day,
                        )
                    }
                    copy(
                        startSelection = FiltersState.SelectionDate(
                            year = startDate.year,
                            month = startDate.month.number,
                            day = startDate.day
                        ),
                        endSelection = endSelection,
                        filter = event.filter,
                    )
                }
            }

            is FiltersEvent.Internal.SetMinMaxYearsPeriod -> state.reduceWith(event) {
                val gridItems = mutableListOf<FiltersState.GridItem>()
                val months =
                    resourceManager.value.getStringArray(Resources.array.reports_month_names)
                var initialScrollIndex = 0
                val scrollDateNumber = if (state.startSelection != null) {
                    comparableDate(
                        year = state.startSelection.year,
                        month = state.startSelection.month,
                        day = 1,
                    )
                } else {
                    -1
                }
                for (year in (event.startYear..event.endYear)) {
                    for (month in Month.entries) {
                        val date = LocalDate(year, month, 1)
                        val dateNumber = comparableDate(year = year, month = month.number, day = 1)
                        val daysInMonth = date.daysInMonth()
                        if (scrollDateNumber > dateNumber) {
                            initialScrollIndex += daysInMonth + date.dayOfWeek.ordinal + 1
                        }
                        FiltersState.GridItem(
                            title = "${months[month.ordinal]} $year",
                            month = month.number,
                            year = year,
                            endDay = date.daysInMonth(),
                            padStart = date.dayOfWeek.ordinal,
                        ).let(gridItems::add)
                    }
                }
                state {
                    copy(
                        grid = gridItems.toImmutableList(),
                        initialScrollIndex = initialScrollIndex
                    )
                }
            }

            is FiltersEvent.External.DateClicked -> state.reduceWith(event) {
                val eventSelection = FiltersState
                    .SelectionDate(year = event.year, month = event.month, day = event.day)
                val eventDate =
                    comparableDate(year = event.year, month = event.month, day = event.day)

                val (newStart, newEnd) = when {
                    state.startSelection != null && state.endSelection != null -> {
                        eventSelection to null
                    }

                    state.startSelection == null -> {
                        eventSelection to null
                    }

                    else -> {
                        val start = state.startSelection
                        val startDate =
                            comparableDate(year = start.year, month = start.month, day = start.day)
                        if (eventDate > startDate) {
                            start to eventSelection
                        } else {
                            eventSelection to null
                        }
                    }
                }
                state {
                    copy(
                        startSelection = newStart,
                        endSelection = newEnd,
                        filter = FilterEntityFactory.fromPeriod(
                            period = calculatePeriod(newStart, newEnd),
                            tags = state.filter.tags,
                            resourceManager = resourceManager.value,
                        )
                    )
                }
            }
        }
    }

    private fun comparableDate(year: Int, month: Int, day: Int): Int {
        @Suppress("MagicNumber")
        return year * 10_000 + month * 100 + day
    }

    private fun calculatePeriod(
        start: FiltersState.SelectionDate,
        end: FiltersState.SelectionDate?,
    ): PeriodTimestampEntity {
        val tz = TimeZone.currentSystemDefault()
        val startDate = LocalDateTime(
            year = start.year,
            month = start.month,
            day = start.day,
            hour = 0,
            minute = 0,
        ).toInstant(tz)
        val endDate = if (end == null) {
            startDate
                .plus(1, DateTimeUnit.DAY, tz)
                .minus(1, DateTimeUnit.MILLISECOND, tz)
        } else {
            LocalDateTime(
                year = end.year,
                month = end.month,
                day = end.day,
                hour = 0,
                minute = 0,
            )
                .toInstant(tz)
                .plus(1, DateTimeUnit.DAY, tz)
                .minus(1, DateTimeUnit.MILLISECOND, tz)
        }
        return PeriodTimestampEntity(
            from = startDate.toEpochMilliseconds(),
            to = endDate.toEpochMilliseconds(),
        )
    }
}
