package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface ObserveTagsUseCase {
    suspend operator fun invoke(): ObserveTagsUseCaseResult
}

sealed interface ObserveTagsUseCaseResult {
    data class Success(val tags: Flow<ImmutableList<TagEntity>>) : ObserveTagsUseCaseResult
    data object Failure : ObserveTagsUseCaseResult
}
