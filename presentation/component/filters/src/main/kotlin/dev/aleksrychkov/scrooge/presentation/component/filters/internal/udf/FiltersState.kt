package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class FiltersState(
    val filter: FilterEntity = FilterEntity(),
    val filterReadable: String = "",

    val allYears: ImmutableList<Int> = persistentListOf(),
    val selectedYear: Int = 0,
    val allMonths: ImmutableList<String> = persistentListOf(),
    val selectedMonthNumber: Int = 1,
)
