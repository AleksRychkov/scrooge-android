package dev.aleksrychkov.scrooge.core.entity

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import kotlinx.datetime.until
import kotlinx.serialization.Serializable

private const val DAYS_IN_DECEMBER = 31

@Serializable
data class PeriodDatestampEntity(val from: Datestamp, val to: Datestamp)

fun startEndOfYear(year: Int): PeriodDatestampEntity {
    val from = Datestamp.from(LocalDate(year = year, Month.JANUARY, 1))
    val to = Datestamp.from(LocalDate(year = year, Month.DECEMBER, DAYS_IN_DECEMBER))
    return PeriodDatestampEntity(from = from, to = to)
}

fun startEndOfMonth(month: Month, year: Int): PeriodDatestampEntity {
    val ld = LocalDate(year = year, month = month, 1)
    val from = Datestamp.from(ld)
    val to = Datestamp.from(LocalDate(year = year, month = month, ld.daysInMonth()))

    return PeriodDatestampEntity(from = from, to = to)
}

fun LocalDate.daysInMonth(): Int {
    val start = LocalDate(year, month, 1)
    val end = start.plus(1, DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY).toInt()
}
