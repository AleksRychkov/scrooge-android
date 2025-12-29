package dev.aleksrychkov.scrooge.presentation.component.filters.internal.utils

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.resources.R
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.daysInMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

internal class FiltersReadableNameHelper(
    private val resourceManager: ResourceManager,
) {
    private var _months: Array<String>? = null
    private var _shortMonths: Array<String>? = null

    fun getName(filter: FilterEntity): String {
        val months = getMonths()
        val shortMonths = getShortMonths()
        val startDate = filter.period.from.date
        val endDate = filter.period.to.date
        val name = when {
            // same day, month, year
            startDate == endDate ->
                "${startDate.day} ${months[startDate.month.ordinal]} ${startDate.year}"

            // same year, same full month
            startDate.year == endDate.year &&
                startDate.month == endDate.month &&
                startDate.daysInMonth() == endDate.day ->
                "${months[startDate.month.ordinal]} ${startDate.year}"

            // same year, same partial month
            startDate.year == endDate.year && startDate.month == endDate.month ->
                "${startDate.day} - ${endDate.day}" +
                    " " +
                    "${months[startDate.month.ordinal]} ${startDate.year}"

            // same year
            startDate.year == endDate.year ->
                "${startDate.day} " +
                    shortMonths[startDate.month.ordinal] +
                    " - " +
                    "${endDate.day} " +
                    shortMonths[endDate.month.ordinal] +
                    " " +
                    "${startDate.year}"

            // default
            else ->
                "${startDate.toReadable()} - ${endDate.toReadable()}"
        }
        return name
    }

    private fun getMonths(): Array<String> {
        if (_months == null) {
            _months = resourceManager.getStringArray(R.array.month_names)
        }
        return _months!!
    }

    private fun getShortMonths(): Array<String> {
        if (_shortMonths == null) {
            _shortMonths = resourceManager.getStringArray(R.array.short_month_names)
        }
        return _shortMonths!!
    }

    private fun LocalDate.toReadable(): String =
        this.format(
            LocalDate.Format {
                day()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
}
