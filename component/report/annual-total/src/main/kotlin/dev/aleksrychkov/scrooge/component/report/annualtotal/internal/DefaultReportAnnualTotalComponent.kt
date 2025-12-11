package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.TotalMonthlyComponent
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

internal class DefaultReportAnnualTotalComponent(
    componentContext: ComponentContext
) : ReportAnnualTotalComponentInternal, ComponentContext by componentContext {

    private val currentYear: Int =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    private val _state = MutableStateFlow(
        ReportAnnualTotalState(
            selectedYear = currentYear
        )
    )

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("ReportAnnualPeriodTotalComponentContext")
        ).also {
            val startEnd = getStartEndForYear(state.value.selectedYear)
            it.setPeriod(fromTimestamp = startEnd.first, toTimestamp = startEnd.second)
        }
    }

    private val _totalMonthlyComponent: TotalMonthlyComponent by lazy {
        TotalMonthlyComponent(
            componentContext = childContext("ReportAnnualTotalMonthlyComponentContext")
        ).also {
            it.setYear(state.value.selectedYear)
        }
    }

    override val state: StateFlow<ReportAnnualTotalState>
        get() = _state.asStateFlow()

    override val periodTotalComponent: PeriodTotalComponent
        get() = _periodTotalComponent

    override val totalMonthlyComponent: TotalMonthlyComponent
        get() = _totalMonthlyComponent

    @Suppress("EmptyFunctionBlock")
    override fun openPeriodModal() {
    }

    override fun incrementYear() {
        val selectedYear = state.value.selectedYear + 1
        val startEnd = getStartEndForYear(selectedYear)
        _periodTotalComponent.setPeriod(
            fromTimestamp = startEnd.first,
            toTimestamp = startEnd.second
        )
        _totalMonthlyComponent.setYear(selectedYear)
        _state.value = ReportAnnualTotalState(selectedYear = selectedYear)
    }

    override fun decrementYear() {
        val selectedYear = state.value.selectedYear - 1
        val startEnd = getStartEndForYear(selectedYear)
        _periodTotalComponent.setPeriod(
            fromTimestamp = startEnd.first,
            toTimestamp = startEnd.second
        )
        _totalMonthlyComponent.setYear(selectedYear)
        _state.value = ReportAnnualTotalState(selectedYear = selectedYear)
    }

    override fun currentYear() {
        if (state.value.selectedYear != currentYear) {
            val startEnd = getStartEndForYear(currentYear)
            _periodTotalComponent.setPeriod(
                fromTimestamp = startEnd.first,
                toTimestamp = startEnd.second
            )
            _totalMonthlyComponent.setYear(currentYear)
            _state.value = ReportAnnualTotalState(selectedYear = currentYear)
        }
    }

    private fun getStartEndForYear(year: Int): Pair<Long, Long> {
        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now().toLocalDateTime(tz)
        val startMillis = LocalDateTime(year, 1, 1, 0, 0)
            .toInstant(tz)
            .toEpochMilliseconds()
        val endMillis = LocalDateTime(now.year + 1, 1, 1, 0, 0)
            .toInstant(tz)
            .minus(1, DateTimeUnit.MILLISECOND)
            .toEpochMilliseconds()

        return startMillis to endMillis
    }
}
