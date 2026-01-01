package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

@Immutable
internal data class ReportAnnualTotalState(
    val filter: FilterEntity = FilterEntity(),
    val initialFilters: FilterEntity = FilterEntity(),
)
