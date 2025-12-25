package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.period.PeriodComponent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.totalMonthly.TotalMonthlyComponent
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.core.entity.startEndOfYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

internal class DefaultReportAnnualTotalComponent(
    componentContext: ComponentContext
) : ReportAnnualTotalComponentInternal, ComponentContext by componentContext {

    private val currentYear: Int =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    private val periodNavigation = SlotNavigation<Int>()

    private val _state = MutableStateFlow(
        ReportAnnualTotalState(
            selectedYear = currentYear
        )
    )

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("ReportAnnualPeriodTotalComponentContext")
        ).also {
            it.setPeriod(period = startEndOfYear(state.value.selectedYear))
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

    override val periodModal: Value<ChildSlot<*, PeriodComponent>> =
        childSlot(
            source = periodNavigation,
            serializer = null,
            handleBackButton = true,
            key = "ReportAnnualTotalPeriodModalSlot",
        ) { year, childComponentContext ->
            PeriodComponent(
                componentContext = childComponentContext,
                year = year,
            )
        }

    override fun openPeriodModal() {
        periodNavigation.activate(_state.value.selectedYear)
    }

    override fun closePeriodModal() {
        periodNavigation.dismiss()
    }

    override fun setPeriod(year: Int) {
        _periodTotalComponent.setPeriod(period = startEndOfYear(year))
        _totalMonthlyComponent.setYear(year)
        _state.value = ReportAnnualTotalState(selectedYear = year)
    }
}
