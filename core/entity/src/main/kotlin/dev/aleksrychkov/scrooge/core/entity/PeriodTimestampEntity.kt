package dev.aleksrychkov.scrooge.core.entity

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class PeriodTimestampEntity(val from: Long, val to: Long)

fun startEndOfYear(year: Int): PeriodTimestampEntity {
    val tz = TimeZone.currentSystemDefault()
    val startMillis = LocalDateTime(year, 1, 1, 0, 0)
        .toInstant(tz)
        .toEpochMilliseconds()
    val endMillis = LocalDateTime(year + 1, 1, 1, 0, 0)
        .toInstant(tz)
        .minus(1, DateTimeUnit.MILLISECOND)
        .toEpochMilliseconds()

    return PeriodTimestampEntity(from = startMillis, to = endMillis)
}

fun startEndOfMonth(month: Month, year: Int): PeriodTimestampEntity {
    val tz = TimeZone.currentSystemDefault()
    val startMillis = LocalDateTime(year, month.number, 1, 0, 0)
        .toInstant(tz)
        .toEpochMilliseconds()
    val endMillis = if (month.number == Month.DECEMBER.number) {
        LocalDateTime(year + 1, 1, 1, 0, 0)
    } else {
        LocalDateTime(year, month.number + 1, 1, 0, 0)
    }
        .toInstant(tz)
        .minus(1, DateTimeUnit.MILLISECOND)
        .toEpochMilliseconds()

    return PeriodTimestampEntity(from = startMillis, to = endMillis)
}

fun startEndOfMonth(instant: Instant): PeriodTimestampEntity {
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

    return PeriodTimestampEntity(from = startMillis, to = endMillis)
}
