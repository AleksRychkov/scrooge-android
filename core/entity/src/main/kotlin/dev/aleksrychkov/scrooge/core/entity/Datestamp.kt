package dev.aleksrychkov.scrooge.core.entity

import dev.aleksrychkov.scrooge.core.entity.Datestamp.Companion.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
data class Datestamp(val value: Long) {
    companion object Companion {
        private const val YEAR_MULTIPLIER = 10_000L
        private const val MONTH_MULTIPLIER = 100L

        fun from(date: LocalDate): Datestamp {
            val date = date.year * YEAR_MULTIPLIER + date.month.number * MONTH_MULTIPLIER + date.day
            return Datestamp(value = date)
        }

        fun from(
            instant: Instant,
            timeZone: TimeZone = TimeZone.currentSystemDefault()
        ): Datestamp {
            return from(date = instant.toLocalDateTime(timeZone = timeZone).date)
        }

        fun now(timeZone: TimeZone = TimeZone.currentSystemDefault()): Datestamp {
            return from(instant = Clock.System.now(), timeZone = timeZone)
        }

        fun LocalDate.readableName(): String =
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

    val date: LocalDate
        get() {
            val year = (value / YEAR_MULTIPLIER).toInt()
            val month = ((value / MONTH_MULTIPLIER) % MONTH_MULTIPLIER).toInt()
            val day = (value % MONTH_MULTIPLIER).toInt()
            return LocalDate(year = year, month = month, day = day)
        }

    fun toInstant(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant =
        date.atStartOfDayIn(timeZone = timeZone)

    fun readableName(): String = date.readableName()

}

fun Datestamp.readableName(
    today: String,
    yesterday: String,
): String {
    val now = now()
    return when {
        now == this -> today
        this.date == now.date.minus(1, DateTimeUnit.DAY) -> yesterday
        else -> this.readableName()
    }
}
