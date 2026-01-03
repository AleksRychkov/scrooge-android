package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.startEndOf
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.utils.FiltersReadableNameHelper
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlin.math.max
import kotlin.math.min
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class FiltersReducer(
    private val resourceManager: Lazy<ResourceManager> = getLazy()
) : Reducer<FiltersState, FiltersEvent, FiltersCommand, FiltersEffect> {

    private val readableNameHelper: FiltersReadableNameHelper by lazy {
        FiltersReadableNameHelper(resourceManager = resourceManager.value)
    }

    private val now: LocalDate by lazy { Datestamp.now().date }

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
                    copy(
                        settings = event.settings,
                        filter = event.filter,
                        initialFilter = event.filter.copy(),
                        filterReadable = readableNameHelper.getName(filter = event.filter),
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
                val selectedMonths = state.filter.months.toMutableList()
                val monthNumber = event.month
                if (event.isLongClick) {
                    var min = selectedMonths.min()
                    var max = selectedMonths.max()
                    if (monthNumber < min) {
                        min = monthNumber
                    } else if (monthNumber > max) {
                        max = monthNumber
                    } else {
                        if (monthNumber <= Month.JUNE.number) {
                            min = min(monthNumber + 1, Month.JUNE.number)
                        } else {
                            max = max(monthNumber - 1, Month.JULY.number)
                        }
                    }
                    selectedMonths.clear()
                    selectedMonths.addAll(min..max)
                } else {
                    selectedMonths.clear()
                    selectedMonths.add(monthNumber)
                }
                val period = startEndOf(
                    startMonth = selectedMonths.minOrNull() ?: Month.JANUARY.number,
                    startYear = state.filter.years.min(),
                    endMonth = selectedMonths.maxOrNull() ?: Month.DECEMBER.number,
                    endYear = state.filter.years.max(),
                )

                val filter = state.filter.copy(
                    period = period,
                    months = selectedMonths.toImmutableList(),
                )
                state {
                    copy(
                        filter = filter,
                        filterReadable = readableNameHelper.getName(filter = filter),
                    )
                }
            }

            is FiltersEvent.External.YearClicked -> state.reduceWith(event) {
                val selectedYears = state.filter.years.toMutableList()
                val year = event.year
                val resetMonths = selectedYears.size > 1
                val selectedMonths = state.filter.months.toMutableList()
                if (event.isLongClick) {
                    var min = selectedYears.min()
                    var max = selectedYears.max()
                    if (year < min) {
                        min = year
                    } else if (year > max) {
                        max = year
                    } else {
                        val index = selectedYears.indexOf(year)
                        if (index <= selectedYears.size / 2) {
                            min = year
                        } else {
                            max = year
                        }
                    }
                    selectedYears.clear()
                    selectedYears.addAll(min..max)

                    if (selectedYears.size > 1) {
                        selectedMonths.clear()
                        selectedMonths.addAll(Month.JANUARY.number..Month.DECEMBER.number)
                    }
                } else {
                    selectedYears.clear()
                    selectedYears.add(year)

                    if (resetMonths) {
                        selectedMonths.clear()
                        selectedMonths.add(now.month.number)
                    }
                }
                val period = startEndOf(
                    startMonth = selectedMonths.minOrNull() ?: Month.JANUARY.number,
                    startYear = selectedYears.min(),
                    endMonth = selectedMonths.maxOrNull() ?: Month.DECEMBER.number,
                    endYear = selectedYears.max(),
                )
                val filter = state.filter.copy(
                    period = period,
                    years = selectedYears.toImmutableList(),
                    months = selectedMonths.toImmutableList(),
                )
                state {
                    copy(
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
                    copy(
                        filter = filter,
                        filterReadable = readableNameHelper.getName(filter = filter),
                        selectedTags = filter.tags,
                    )
                }
            }
        }
    }
}
