package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class ObserveTagsDelegate(
    private val observeTagsUseCase: Lazy<ObserveTagsUseCase> = getLazy(),
) {
    suspend operator fun invoke(): Flow<TagEvent> {
        return when (val result = observeTagsUseCase.value()) {
            ObserveTagsUseCaseResult.Failure -> emptyFlow()
            is ObserveTagsUseCaseResult.Success -> result.tags.map { TagEvent.Internal.Tags(it) }
        }
    }
}
