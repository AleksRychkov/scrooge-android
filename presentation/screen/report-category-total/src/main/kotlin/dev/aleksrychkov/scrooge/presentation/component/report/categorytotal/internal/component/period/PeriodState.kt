package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.period

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Immutable
internal data class PeriodState(
    val period: PeriodTimestampEntity,
    val name: String,
) {
    companion object {
        operator fun invoke(
            period: PeriodTimestampEntity,
            resourceManager: ResourceManager,
        ): PeriodState {
            return PeriodState(
                period = period,
                name = period.toReadableName(resourceManager),
            )
        }
    }
}

private fun PeriodTimestampEntity.toReadableName(
    resourceManager: ResourceManager,
): String {
    val months = resourceManager.getStringArray(Resources.array.reports_month_names)
    fun sameYear(
        year: Int,
        startMonth: Month,
        endMonth: Month
    ): String {
        return "${months[startMonth.ordinal]} - ${months[endMonth.ordinal]} $year"
    }

    fun sameMonthAndYear(
        year: Int,
        month: Month
    ): String {
        return "${months[month.ordinal]} $year"
    }

    fun custom(
        start: LocalDate,
        end: LocalDate
    ): String {
        return "${start.day}.${start.month.number}.${start.year} - ${end.day}.${end.month.number}.${end.year}"
    }

    val tz = TimeZone.currentSystemDefault()
    val from = Instant
        .fromEpochMilliseconds(from)
        .toLocalDateTime(tz)
        .date
    val to = Instant
        .fromEpochMilliseconds(to)
        .toLocalDateTime(tz)
        .date

    @Suppress("IntroduceWhenSubject")
    return when {
        from.year == to.year && from.month == to.month
        -> sameMonthAndYear(year = from.year, month = from.month)

        from.year == to.year
        -> sameYear(year = from.year, startMonth = from.month, endMonth = to.month)

        else
        -> custom(start = from, end = to)
    }
}
