package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class ObserveTagsDelegate(
    private val observeTagsUseCase: Lazy<ObserveTagsUseCase>,
) {
    suspend operator fun invoke(): Flow<TagEvent> {
        val result = observeTagsUseCase.value()
        return when (result) {
            ObserveTagsUseCaseResult.Failure -> flowOf(TagEvent.Internal.FailedToObserveTags)
            is ObserveTagsUseCaseResult.Success -> result.tags.map { TagEvent.Internal.Tags(it) }
        }
    }
}
