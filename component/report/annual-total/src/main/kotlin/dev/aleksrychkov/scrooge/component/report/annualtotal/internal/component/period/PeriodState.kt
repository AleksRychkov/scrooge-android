package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.period

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class PeriodState(
    val selectedYear: Int,
    val allYears: ImmutableList<Int>,
    val currentYear: Int,
)
