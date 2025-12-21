package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity

internal sealed interface PeriodTotalEvent {
    sealed interface External : PeriodTotalEvent {
        data class Load(val fromTimestamp: Long, val toTimestamp: Long) : External
    }

    sealed interface Internal : PeriodTotalEvent {
        data class LoadSuccess(val result: ReportTotalAmountEntity) : Internal
        data object LoadFailed : Internal
    }
}
