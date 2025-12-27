package dev.aleksrychkov.scrooge.presentation.component.filters.internal

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.until

internal fun LocalDate.daysInMonth(): Int {
    val start = LocalDate(year, month, 1)
    val end = start.plus(1, DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY).toInt()
}
