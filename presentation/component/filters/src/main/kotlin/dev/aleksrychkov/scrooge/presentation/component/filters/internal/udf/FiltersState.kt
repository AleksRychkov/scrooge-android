package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class FiltersState(
    val startSelection: SelectionDate? = null,
    val endSelection: SelectionDate? = null,
    val grid: ImmutableList<GridItem> = persistentListOf(),
) {

    @Immutable
    data class GridItem(
        val title: String,
        val year: Int,
        val month: Int,
        val padStart: Int,
        val endDay: Int,
    )

    @Immutable
    data class SelectionDate(
        val year: Int,
        val month: Int,
        val day: Int,
    )
}
