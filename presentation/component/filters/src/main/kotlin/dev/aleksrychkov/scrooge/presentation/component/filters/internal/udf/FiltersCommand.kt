package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface FiltersCommand {
    data object GetFiltersStartEndYears : FiltersCommand
    data class ResolveCurrency(
        val filter: FilterEntity,
        val submitWhenResolved: Boolean = false,
    ) : FiltersCommand
}
