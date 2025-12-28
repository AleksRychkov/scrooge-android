package dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity

internal sealed interface PeriodTotalEvent {
    sealed interface External : PeriodTotalEvent {
        data class Load(val filter: FilterEntity) : External
    }

    sealed interface Internal : PeriodTotalEvent {
        data class LoadSuccess(val result: ReportTotalAmountEntity) : Internal
        data object LoadFailed : Internal
    }
}
