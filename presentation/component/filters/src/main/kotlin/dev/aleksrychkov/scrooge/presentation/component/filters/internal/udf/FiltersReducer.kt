package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.until
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class FiltersReducer(
    private val resourceManager: Lazy<ResourceManager> = getLazy()
) : Reducer<FiltersState, FiltersEvent, FiltersCommand, Unit> {
    override fun reduce(
        event: FiltersEvent,
        state: FiltersState
    ): ReducerResult<FiltersState, FiltersCommand, Unit> {
        return when (event) {
            FiltersEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(FiltersCommand.GetFiltersStartEndYears)
                }
            }

            is FiltersEvent.Internal.SetMinMaxYearsPeriod -> state.reduceWith(event) {
                val gridItems = mutableListOf<FiltersState.GridItem>()
                val months =
                    resourceManager.value.getStringArray(Resources.array.reports_month_names)
                for (year in (event.startYear..event.endYear)) {
                    for (month in Month.entries) {
                        val date = LocalDate(year, month, 1)
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
                    copy(grid = gridItems.toImmutableList())
                }
            }

            is FiltersEvent.External.DateClicked -> state.reduceWith(event) {
                val eventSelection = FiltersState.SelectionDate(
                    year = event.year,
                    month = event.month,
                    day = event.day
                )

                @Suppress("MagicNumber")
                val eventDate = event.year * 10_000 + event.month * 100 + event.day

                val (newStart, newEnd) = when {
                    state.startSelection != null && state.endSelection != null -> {
                        eventSelection to null
                    }

                    state.startSelection == null -> {
                        eventSelection to null
                    }

                    else -> {
                        val start = state.startSelection

                        @Suppress("MagicNumber")
                        val startDate = start.year * 10_000 + start.month * 100 + start.day

                        if (eventDate > startDate) {
                            start to eventSelection
                        } else {
                            eventSelection to null
                        }
                    }
                }
                state {
                    copy(startSelection = newStart, endSelection = newEnd)
                }
            }
        }
    }

    private fun LocalDate.daysInMonth(): Int {
        val start = LocalDate(year, month, 1)
        val end = start.plus(1, DateTimeUnit.MONTH)
        return start.until(end, DateTimeUnit.DAY).toInt()
    }
}
