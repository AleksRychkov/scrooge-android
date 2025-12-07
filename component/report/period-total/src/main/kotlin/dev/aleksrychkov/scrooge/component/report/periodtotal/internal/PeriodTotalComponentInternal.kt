package dev.aleksrychkov.scrooge.component.report.periodtotal.internal

import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalState
import kotlinx.coroutines.flow.StateFlow

internal interface PeriodTotalComponentInternal : PeriodTotalComponent {
    val state: StateFlow<PeriodTotalState>
}
