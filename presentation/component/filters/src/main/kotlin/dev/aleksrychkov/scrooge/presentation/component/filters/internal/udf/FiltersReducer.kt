package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class FiltersReducer : Reducer<FiltersState, FiltersEvent, FiltersCommand, Unit> {
    override fun reduce(
        event: FiltersEvent,
        state: FiltersState
    ): ReducerResult<FiltersState, FiltersCommand, Unit> {
        return when (event) {
            FiltersEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(FiltersCommand.GetFiltersStartEndYears)
                }
            }
        }
    }
}
