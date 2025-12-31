package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersCommand
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

internal class LoadTagsDelegate(
    private val tagsUseCase: Lazy<ObserveTagsUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: FiltersCommand.GetAvailableTags): Flow<FiltersEvent> {
        val tagsTags = when (val res = tagsUseCase.value.invoke()) {
            ObserveTagsUseCaseResult.Failure -> persistentSetOf()
            is ObserveTagsUseCaseResult.Success -> res.tags.firstOrNull() ?: persistentSetOf()
        }

        return flowOf(
            FiltersEvent.Internal.AvailableTags(tags = tagsTags)
        )
    }
}
