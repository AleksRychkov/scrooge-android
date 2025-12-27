package dev.aleksrychkov.scrooge.presentation.component.filters

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.startEndOfMonth
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.daysInMonth
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

object FilterEntityFactory {

    private var _months: Array<String>? = null

    fun currentMonth(
        resourceManager: ResourceManager,
        tags: ImmutableSet<String> = persistentSetOf(),
    ): FilterEntity {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val period = startEndOfMonth(month = today.month, year = today.year)
        val months = getMonths(resourceManager)

        return FilterEntity(
            readableName = "${months[today.month.ordinal]} ${today.year}",
            period = period,
            tags = tags,
        )
    }

    fun fromPeriod(
        period: PeriodTimestampEntity,
        resourceManager: ResourceManager,
        tags: ImmutableSet<String> = persistentSetOf(),
    ): FilterEntity {
        val months = getMonths(resourceManager)
        val tz = TimeZone.currentSystemDefault()
        val startDate = Instant
            .fromEpochMilliseconds(period.from)
            .toLocalDateTime(tz)
            .date
        val endDate = Instant
            .fromEpochMilliseconds(period.to)
            .toLocalDateTime(tz)
            .date

        return when {
            // same day, month, year
            startDate == endDate -> {
                FilterEntity(
                    readableName = "${startDate.day} ${months[startDate.month.ordinal]} ${startDate.year}",
                    period = period,
                    tags = tags,
                )
            }

            // same year, same full month
            startDate.year == endDate.year &&
                startDate.month == endDate.month &&
                startDate.daysInMonth() == endDate.day -> {
                FilterEntity(
                    readableName = "${months[startDate.month.ordinal]} ${startDate.year}",
                    period = period,
                    tags = tags,
                )
            }

            // same year, same partial month
            startDate.year == endDate.year &&
                startDate.month == endDate.month -> {
                FilterEntity(
                    readableName = "${startDate.day} - ${endDate.day}" +
                        " " +
                        "${months[startDate.month.ordinal]} ${startDate.year}",
                    period = period,
                    tags = tags,
                )
            }

            // same year
            startDate.year == endDate.year -> {
                @Suppress("MagicNumber")
                FilterEntity(
                    readableName = "${startDate.day} " +
                        months[startDate.month.ordinal].take(3).padEnd(4, '.') +
                        " - " +
                        "${endDate.day} " +
                        months[endDate.month.ordinal].take(3).padEnd(4, '.') +
                        " " +
                        "${startDate.year}",
                    period = period,
                    tags = tags,
                )
            }

            // default
            else -> {
                FilterEntity(
                    readableName = "${startDate.toReadable()} - ${endDate.toReadable()}",
                    period = period,
                    tags = tags,
                )
            }
        }
    }

    private fun getMonths(resourceManager: ResourceManager): Array<String> {
        if (_months == null) {
            _months = resourceManager.getStringArray(Resources.array.reports_month_names)
        }
        return _months!!
    }

    private fun Int.toDateString(): String = this.toString().padStart(2, '0')

    private fun LocalDate.toReadable(): String =
        StringBuilder()
            .append(this.day)
            .append('.')
            .append(this.month.number.toDateString())
            .append('.')
            .append(this.year)
            .toString()
}
