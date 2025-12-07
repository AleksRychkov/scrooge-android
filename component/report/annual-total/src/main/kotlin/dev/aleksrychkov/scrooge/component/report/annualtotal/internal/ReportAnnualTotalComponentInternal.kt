package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import kotlinx.coroutines.flow.StateFlow

internal interface ReportAnnualTotalComponentInternal : ReportAnnualTotalComponent {
    val state: StateFlow<ReportAnnualTotalState>

    fun openPeriodModal()
    fun incrementYear()
    fun decrementYear()
    fun currentYear()
}
