package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import kotlinx.coroutines.flow.StateFlow

internal interface ReportAnnualTotalComponentInternal : ReportAnnualTotalComponent {
    val state: StateFlow<ReportAnnualTotalState>

    val periodTotalComponent: PeriodTotalComponent

    fun openPeriodModal()
    fun incrementYear()
    fun decrementYear()
    fun currentYear()
}
