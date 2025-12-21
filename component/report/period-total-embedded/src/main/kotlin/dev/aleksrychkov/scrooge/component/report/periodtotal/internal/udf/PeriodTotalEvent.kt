package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity

internal sealed interface PeriodTotalEvent {
    sealed interface External : PeriodTotalEvent {
        data class Load(val period: PeriodTimestampEntity) : External
    }

    sealed interface Internal : PeriodTotalEvent {
        data class LoadSuccess(val result: ReportTotalAmountEntity) : Internal
        data object LoadFailed : Internal
    }
}
