package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

internal sealed interface FiltersCommand {
    data object GetFiltersStartEndYears : FiltersCommand
}
