package dev.aleksrychkov.scrooge.component.transactionform.internal.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal fun Instant.toDateString(): String =
    this.toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .format(
            LocalDate.Format {
                day(Padding.ZERO)
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
