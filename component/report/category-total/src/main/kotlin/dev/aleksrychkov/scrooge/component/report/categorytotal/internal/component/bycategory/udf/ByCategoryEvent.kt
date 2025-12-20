package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

internal sealed interface ByCategoryEvent {
    sealed interface External : ByCategoryEvent {
        data class Load(val period: PeriodTimestampEntity) : External
    }

    sealed interface Internal : ByCategoryEvent {
        data object LoadSuccess : Internal
        data object LoadFailed : Internal
    }
}
