package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalActor
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalEvent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalReducer
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow
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

    private val currentYear: Int
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    private val store: Store<ReportAnnualTotalState, ReportAnnualTotalEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = ReportAnnualTotalState(),
            actor = ReportAnnualTotalActor(),
            reducer = ReportAnnualTotalReducer(),
            startEvent = ReportAnnualTotalEvent.External.Initial(year = currentYear),
        )
    }

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("ReportAnnualPeriodTotalComponentContext")
        ).also {
            val startEnd = getStartEndForYear(state.value.selectedYear)
            it.setPeriod(fromTimestamp = startEnd.first, toTimestamp = startEnd.second)
        }
    }

    override val state: StateFlow<ReportAnnualTotalState>
        get() = store.state

    override val periodTotalComponent: PeriodTotalComponent
        get() = _periodTotalComponent

    @Suppress("EmptyFunctionBlock")
    override fun openPeriodModal() {
    }

    override fun incrementYear() {
        store.handle(ReportAnnualTotalEvent.External.IncrementYear)
        val startEnd = getStartEndForYear(state.value.selectedYear + 1)
        _periodTotalComponent.setPeriod(
            fromTimestamp = startEnd.first,
            toTimestamp = startEnd.second
        )
    }

    override fun decrementYear() {
        store.handle(ReportAnnualTotalEvent.External.DecrementYear)
        val startEnd = getStartEndForYear(state.value.selectedYear - 1)
        _periodTotalComponent.setPeriod(
            fromTimestamp = startEnd.first,
            toTimestamp = startEnd.second
        )
    }

    override fun currentYear() {
        if (state.value.selectedYear != currentYear) {
            store.handle(ReportAnnualTotalEvent.External.Initial(year = currentYear))
            val startEnd = getStartEndForYear(currentYear)
            _periodTotalComponent.setPeriod(
                fromTimestamp = startEnd.first,
                toTimestamp = startEnd.second
            )
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
