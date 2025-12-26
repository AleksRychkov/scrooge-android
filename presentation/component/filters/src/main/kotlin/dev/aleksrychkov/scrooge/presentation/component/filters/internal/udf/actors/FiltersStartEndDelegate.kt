package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors

import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersCommand
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class FiltersStartEndDelegate {
    suspend operator fun invoke(cmd: FiltersCommand.GetFiltersStartEndYears): Flow<FiltersEvent> {
        return emptyFlow()
    }
}
