package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.startEndOfMonth
import dev.aleksrychkov.scrooge.core.entity.startEndOfYear
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.utils.FiltersReadableNameHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.Month
import kotlinx.datetime.number
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class FiltersReducer(
    private val resourceManager: Lazy<ResourceManager> = getLazy()
) : Reducer<FiltersState, FiltersEvent, FiltersCommand, FiltersEffect> {

    private val readableNameHelper: FiltersReadableNameHelper by lazy {
        FiltersReadableNameHelper(resourceManager = resourceManager.value)
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun reduce(
        event: FiltersEvent,
        state: FiltersState
    ): ReducerResult<FiltersState, FiltersCommand, FiltersEffect> {
        return when (event) {
            is FiltersEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(FiltersCommand.GetFiltersStartEndYears, FiltersCommand.GetAvailableTags)
                }
                state {
                    val period = event.filter.period
                    val startDate = period.from.date
                    val endDate = period.to.date
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
                        settings = event.settings,
                        filter = event.filter,
                        initialFilter = event.filter.copy(),
                        filterReadable = readableNameHelper.getName(filter = event.filter),
                        selectedYear = selectedYear,
                        selectedMonthNumber = selectedMonth,
                        selectedTags = event.filter.tags,
                    )
                }
            }

            is FiltersEvent.Internal.SetMinMaxYearsPeriod -> state.reduceWith(event) {
                state {
                    copy(
                        allYears = (event.startYear..event.endYear).toImmutableList(),
                        allMonths = resourceManager.value
                            .getStringArray(Resources.array.short_month_names)
                            .toImmutableList(),
                    )
                }
            }

            is FiltersEvent.External.MonthClicked -> state.reduceWith(event) {
                val period = startEndOfMonth(month = Month(event.month), state.selectedYear)
                val filter = state.filter.copy(period = period)
                state {
                    copy(
                        selectedMonthNumber = event.month,
                        filter = filter,
                        filterReadable = readableNameHelper.getName(filter = filter),
                    )
                }
            }

            is FiltersEvent.External.YearClicked -> state.reduceWith(event) {
                val period = when (state.selectedMonthNumber) {
                    -1 -> startEndOfYear(event.year)
                    else -> startEndOfMonth(month = Month(state.selectedMonthNumber), event.year)
                }
                val filter = state.filter.copy(period = period)
                state {
                    copy(
                        selectedYear = event.year,
                        filter = filter,
                        filterReadable = readableNameHelper.getName(filter = filter),
                    )
                }
            }

            is FiltersEvent.Internal.AvailableTags -> state.reduceWith(event) {
                state {
                    copy(allTags = event.tags)
                }
            }

            is FiltersEvent.External.ToggleTag -> state.reduceWith(event) {
                val selectedTags = state.selectedTags.toMutableSet()
                if (selectedTags.contains(event.tag)) {
                    selectedTags.remove(event.tag)
                } else {
                    selectedTags.add(event.tag)
                }
                val res = selectedTags.toImmutableSet()
                state {
                    copy(
                        selectedTags = res,
                        filter = filter.copy(tags = res)
                    )
                }
            }

            is FiltersEvent.External.Reset -> state.reduceWith(event) {
                effects {
                    listOf(FiltersEffect.SubmitFilters(event.filter))
                }
                state {
                    val filter = event.filter
                    val period = filter.period
                    val startDate = period.from.date
                    val endDate = period.to.date
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
                        filter = filter,
                        filterReadable = readableNameHelper.getName(filter = filter),
                        selectedYear = selectedYear,
                        selectedMonthNumber = selectedMonth,
                        selectedTags = filter.tags,
                    )
                }
            }
        }
    }
}
