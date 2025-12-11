package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.period.PeriodComponent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.totalMonthly.TotalMonthlyComponent
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import kotlinx.coroutines.flow.StateFlow

internal interface ReportAnnualTotalComponentInternal : ReportAnnualTotalComponent {
    val periodModal: Value<ChildSlot<*, PeriodComponent>>

    val state: StateFlow<ReportAnnualTotalState>

    val periodTotalComponent: PeriodTotalComponent
    val totalMonthlyComponent: TotalMonthlyComponent

    fun openPeriodModal()
    fun closePeriodModal()
    fun setPeriod(year: Int)
}
