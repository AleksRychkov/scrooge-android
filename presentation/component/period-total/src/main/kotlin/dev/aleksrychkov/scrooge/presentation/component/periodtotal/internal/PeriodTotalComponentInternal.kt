package dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal

import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalState
import kotlinx.coroutines.flow.StateFlow

internal interface PeriodTotalComponentInternal : PeriodTotalComponent {
    val state: StateFlow<PeriodTotalState>
}
