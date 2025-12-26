package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

internal sealed interface FiltersEvent {
    sealed interface External : FiltersEvent {
        data object Init : External
        data class DateClicked(val year: Int, val month: Int, val day: Int) : External
    }

    sealed interface Internal : FiltersEvent {
        data class SetMinMaxYearsPeriod(val startYear: Int, val endYear: Int) : Internal
    }
}
