package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

@Immutable
internal data class ReportCategoryState(
    val filtersName: String = "",
    val filter: FilterEntity = FilterEntity(),
)
