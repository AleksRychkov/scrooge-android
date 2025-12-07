package dev.aleksrychkov.scrooge.component.transaction.root.internal.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal object DateTimeUtils {

    fun getDate(from: Instant): LocalDate {
        val timeZone = TimeZone.currentSystemDefault()
        return from
            .toLocalDateTime(timeZone)
            .date
    }

    fun getMonthStartEndTimestamp(instant: Instant): Pair<Long, Long> {
        val tz = TimeZone.currentSystemDefault()
        val now = instant.toLocalDateTime(tz)
        val startMillis = LocalDateTime(now.year, now.month.number, 1, 0, 0)
            .toInstant(tz)
            .toEpochMilliseconds()
        val endMillis = if (now.month.number == Month.DECEMBER.number) {
            LocalDateTime(now.year + 1, 1, 1, 0, 0)
        } else {
            LocalDateTime(now.year, now.month.number + 1, 1, 0, 0)
        }
            .toInstant(tz)
            .minus(1, DateTimeUnit.MILLISECOND)
            .toEpochMilliseconds()

        return startMillis to endMillis
    }

    fun LocalDate.toInstantAtStartOfDay(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Instant {
        val dateTime = this.atStartOfDayIn(timeZone)
        return dateTime
    }
}
