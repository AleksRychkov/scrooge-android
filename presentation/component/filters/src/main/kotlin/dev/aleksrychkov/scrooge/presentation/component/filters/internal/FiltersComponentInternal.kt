package dev.aleksrychkov.scrooge.presentation.component.filters.internal

import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import kotlinx.coroutines.flow.StateFlow

internal interface FiltersComponentInternal : FiltersComponent {
    val state: StateFlow<FiltersState>
}
