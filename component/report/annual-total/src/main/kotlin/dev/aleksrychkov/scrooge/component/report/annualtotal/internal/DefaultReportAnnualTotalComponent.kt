package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalActor
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalEvent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalReducer
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.TimeZone
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

    override val state: StateFlow<ReportAnnualTotalState>
        get() = store.state

    override fun incrementYear() {
        store.handle(ReportAnnualTotalEvent.External.IncrementYear)
    }

    override fun decrementYear() {
        store.handle(ReportAnnualTotalEvent.External.DecrementYear)
    }

    override fun currentYear() {
        if (state.value.selectedYear != currentYear) {
            store.handle(ReportAnnualTotalEvent.External.Initial(year = currentYear))
        }
    }
}
