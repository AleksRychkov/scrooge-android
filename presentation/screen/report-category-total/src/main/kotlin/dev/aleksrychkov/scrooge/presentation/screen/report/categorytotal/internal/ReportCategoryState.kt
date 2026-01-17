package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

@Immutable
internal data class ReportCategoryState(
    val filter: FilterEntity = FilterEntity(),
)
