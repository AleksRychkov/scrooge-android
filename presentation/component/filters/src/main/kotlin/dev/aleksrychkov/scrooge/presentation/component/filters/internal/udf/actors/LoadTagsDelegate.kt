package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionTagsUseCase
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersCommand
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

internal class LoadTagsDelegate(
    private val transactionTagsUseCase: Lazy<GetTransactionTagsUseCase> = getLazy(),
    private val tagsUseCase: Lazy<ObserveTagsUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: FiltersCommand.GetAvailableTags): Flow<FiltersEvent> {
        val tagsTags = when (val res = tagsUseCase.value.invoke()) {
            ObserveTagsUseCaseResult.Failure ->
                emptySet()

            is ObserveTagsUseCaseResult.Success ->
                res.tags.firstOrNull()?.map(TagEntity::name) ?: emptySet()
        }
        val transactionTags = transactionTagsUseCase.value.invoke()

        return flowOf(
            FiltersEvent.Internal.AvailableTags(tags = (tagsTags + transactionTags).toImmutableSet())
        )
    }
}
