package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity

internal sealed interface PeriodTotalEvent {
    sealed interface External : PeriodTotalEvent {
        data class Load(val fromTimestamp: Long, val toTimestamp: Long) : External
    }

    sealed interface Internal : PeriodTotalEvent {
        data class LoadSuccess(val result: ReportAmountForPeriodByTypeAndCodeEntity) : Internal
        data object LoadFailed : Internal
    }
}
