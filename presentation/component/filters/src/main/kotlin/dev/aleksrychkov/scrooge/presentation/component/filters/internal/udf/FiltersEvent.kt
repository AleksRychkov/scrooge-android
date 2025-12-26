package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

internal sealed interface FiltersEvent {
    sealed interface External : FiltersEvent {
        data object Init : External
    }
}
