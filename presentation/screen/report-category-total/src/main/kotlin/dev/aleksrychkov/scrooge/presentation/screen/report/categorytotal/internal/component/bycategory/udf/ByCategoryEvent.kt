package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity

internal sealed interface ByCategoryEvent {
    sealed interface External : ByCategoryEvent {
        data class Load(val filter: FilterEntity) : External
        data class SetType(val type: Int) : External
        data class BottomSheetOffset(val offset: Float) : External
    }

    sealed interface Internal : ByCategoryEvent {
        data class LoadSuccess(val result: ReportByCategoryEntity) : Internal
        data object LoadFailed : Internal
    }
}
