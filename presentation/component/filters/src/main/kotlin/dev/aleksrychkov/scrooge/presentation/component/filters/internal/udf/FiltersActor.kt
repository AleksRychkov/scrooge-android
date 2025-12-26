package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors.FiltersStartEndDelegate
import kotlinx.coroutines.flow.Flow

internal class FiltersActor(
    private val delegate: FiltersStartEndDelegate = FiltersStartEndDelegate(),
) : Actor<FiltersCommand, FiltersEvent> {
    override suspend fun process(command: FiltersCommand): Flow<FiltersEvent> {
        return when (command) {
            is FiltersCommand.GetFiltersStartEndYears -> delegate.invoke(command)
        }
    }
}
