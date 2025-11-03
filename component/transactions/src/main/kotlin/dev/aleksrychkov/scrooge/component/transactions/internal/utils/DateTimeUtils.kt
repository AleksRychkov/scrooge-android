package dev.aleksrychkov.scrooge.component.transactions.internal.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal object DateTimeUtils {

    fun getDate(from: Instant): LocalDate {
        val timeZone = TimeZone.currentSystemDefault()
        return from
            .toLocalDateTime(timeZone)
            .date
    }

    fun LocalDate.toInstantAtStartOfDay(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Instant {
        val dateTime = this.atStartOfDayIn(timeZone)
        return dateTime
    }
}
