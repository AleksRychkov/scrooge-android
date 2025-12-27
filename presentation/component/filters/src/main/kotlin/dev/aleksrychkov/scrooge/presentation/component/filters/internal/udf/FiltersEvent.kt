package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface FiltersEvent {
    sealed interface External : FiltersEvent {
        data class Init(val filter: FilterEntity) : External
        data class YearClicked(val year: Int) : External
        data class MonthClicked(val month: Int) : External
    }

    sealed interface Internal : FiltersEvent {
        data class SetMinMaxYearsPeriod(val startYear: Int, val endYear: Int) : Internal
    }
}
