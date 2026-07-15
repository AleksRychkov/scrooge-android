package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.IncomeExpenseTimeline
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportIncomeExpenseTimelineEntity
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal object IncomeExpenseTimelineMapper {
    fun toEntity(
        list: List<IncomeExpenseTimeline>,
        period: PeriodDatestampEntity,
        currentDate: LocalDate = Datestamp.now().date,
    ): ReportIncomeExpenseTimelineEntity {
        if (list.isEmpty()) return ReportIncomeExpenseTimelineEntity()
        val valuesByMonth = list.associateBy { row -> row.toMonth() }
        val points = period.months(currentDate).map { month ->
            val row = valuesByMonth[month]
            ReportIncomeExpenseTimelineEntity.Point(
                month = month,
                income = row?.income ?: 0L,
                expense = row?.expense ?: 0L,
            )
        }
        return ReportIncomeExpenseTimelineEntity(points = points.toImmutableList())
    }

    private fun IncomeExpenseTimeline.toMonth(): LocalDate =
        LocalDate(year = year.toInt(), month = month.toInt(), day = 1)

    private fun PeriodDatestampEntity.months(currentDate: LocalDate): List<LocalDate> {
        var current = from.date.let { LocalDate(it.year, it.month, 1) }
        val periodEnd = to.date.let { LocalDate(it.year, it.month, 1) }
        val currentMonth = LocalDate(currentDate.year, currentDate.month, 1)
        val end = minOf(periodEnd, currentMonth)
        return buildList {
            while (current <= end) {
                add(current)
                current = current.plus(1, DateTimeUnit.MONTH)
            }
        }
    }
}
