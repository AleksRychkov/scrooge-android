package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity

internal sealed interface TotalMonthlyEvent {
    sealed interface External : TotalMonthlyEvent {
        data class Load(val year: Int) : TotalMonthlyEvent
    }

    sealed interface Internal : TotalMonthlyEvent {
        data object FailedToLoad : TotalMonthlyEvent
        data class Success(val res: ReportTotalAmountMonthlyEntity) : TotalMonthlyEvent
    }
}
