package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.period

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.component.transaction.root.internal.utils.DateTimeUtils
import dev.aleksrychkov.scrooge.component.transaction.root.internal.utils.DateTimeUtils.toInstantAtStartOfDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlin.time.Clock
import kotlin.time.Instant

@Immutable
internal data class PeriodState(
    val initialYear: Int,
    val selected: Instant,
    val selectedYear: Int,
    val selectedMonth: Int,
) {
    companion object {
        operator fun invoke(): PeriodState {
            val now = Clock.System.now()
            val date = DateTimeUtils.getDate(now)
            return PeriodState(
                initialYear = date.year,
                selected = now,
                selectedYear = date.year,
                selectedMonth = date.month.number,
            )
        }

        fun initial(instant: Instant): PeriodState {
            val date = DateTimeUtils.getDate(instant)
            return PeriodState(
                initialYear = date.year,
                selected = instant,
                selectedYear = date.year,
                selectedMonth = date.month.number,
            )
        }

        operator fun invoke(instant: Instant, initialYear: Int): PeriodState {
            val date = DateTimeUtils.getDate(instant)
            return PeriodState(
                initialYear = initialYear,
                selected = instant,
                selectedYear = date.year,
                selectedMonth = date.month.number,
            )
        }
    }
}

internal fun PeriodState.incrementYear(): PeriodState {
    val date = DateTimeUtils.getDate(this.selected)
    val newDate = date.plus(1, DateTimeUnit.YEAR)
    return PeriodState(newDate.toInstantAtStartOfDay(), initialYear = this.initialYear)
}

internal fun PeriodState.decrementYear(): PeriodState {
    val date = DateTimeUtils.getDate(this.selected)
    val newDate = date.minus(1, DateTimeUnit.YEAR)
    return PeriodState(newDate.toInstantAtStartOfDay(), initialYear = this.initialYear)
}

internal fun PeriodState.setMonth(month: Int): PeriodState {
    val date = DateTimeUtils.getDate(this.selected)
    val newDate = LocalDate(date.year, month, 1)
    return PeriodState(newDate.toInstantAtStartOfDay(), initialYear = this.initialYear)
}
