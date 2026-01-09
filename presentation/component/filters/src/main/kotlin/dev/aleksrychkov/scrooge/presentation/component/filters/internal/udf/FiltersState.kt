package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.EnumSet

@Immutable
internal data class FiltersState(
    val settings: EnumSet<FiltersSettings> = EnumSet.allOf(FiltersSettings::class.java),

    val initialFilter: FilterEntity = FilterEntity(),
    val filter: FilterEntity = FilterEntity(),
    val filterReadable: String = "",

    val allYears: ImmutableList<Int> = persistentListOf(),
    val allMonths: ImmutableList<String> = persistentListOf(),
)
