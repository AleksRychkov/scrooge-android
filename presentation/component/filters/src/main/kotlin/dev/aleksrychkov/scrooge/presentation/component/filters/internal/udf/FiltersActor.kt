package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors.LoadTagsDelegate
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors.StartEndDelegate
import kotlinx.coroutines.flow.Flow

internal class FiltersActor(
    private val startEndDelegate: StartEndDelegate = StartEndDelegate(),
    private val tagsDelegate: LoadTagsDelegate = LoadTagsDelegate(),
) : Actor<FiltersCommand, FiltersEvent> {
    override suspend fun process(command: FiltersCommand): Flow<FiltersEvent> {
        return when (command) {
            is FiltersCommand.GetFiltersStartEndYears -> startEndDelegate.invoke(command)
            is FiltersCommand.GetAvailableTags -> tagsDelegate.invoke(command)
        }
    }
}
