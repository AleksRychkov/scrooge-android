package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity

internal sealed interface ByCategoryEvent {
    sealed interface External : ByCategoryEvent {
        data class Load(val period: PeriodTimestampEntity) : External
        data class SetType(val type: Int) : External
    }

    sealed interface Internal : ByCategoryEvent {
        data class LoadSuccess(val result: ReportByCategoryEntity) : Internal
        data object LoadFailed : Internal
    }
}
