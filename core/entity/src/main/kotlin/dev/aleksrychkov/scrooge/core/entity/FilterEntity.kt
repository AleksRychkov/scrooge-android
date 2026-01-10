package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import kotlinx.serialization.Serializable

@Serializable
data class FilterEntity(
    val period: PeriodDatestampEntity = PeriodDatestampEntity(Datestamp.now(), Datestamp.now()),
    val years: ImmutableList<Int> = persistentListOf(),
    val months: ImmutableList<Int> = persistentListOf(),
    val tags: ImmutableSet<TagEntity> = persistentSetOf(),
    val category: CategoryEntity? = null,
    val transactionType: TransactionType? = null,
) {
    companion object {
        fun currentMonth(): FilterEntity {
            val today = Datestamp.now().date
            val period = startEndOfMonth(month = today.month, year = today.year)
            return FilterEntity(
                period = period,
                years = persistentListOf(today.year),
                months = persistentListOf(today.month.number),
            )
        }

        fun currentYear(): FilterEntity {
            val today = Datestamp.now().date
            val period = startEndOfYear(year = today.year)
            return FilterEntity(
                period = period,
                years = persistentListOf(today.year),
                months = (Month.JANUARY.number..Month.DECEMBER.number).toImmutableList(),
            )
        }
    }
}

fun FilterEntity.readableName(
    months: Array<String>,
    shortMonths: Array<String>,
): String {
    val startDate = this.period.from.date
    val endDate = this.period.to.date
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
