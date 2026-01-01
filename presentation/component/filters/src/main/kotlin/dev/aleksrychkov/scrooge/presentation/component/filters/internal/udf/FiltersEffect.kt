package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface FiltersEffect {
    data class SubmitFilters(val filter: FilterEntity) : FiltersEffect
}
