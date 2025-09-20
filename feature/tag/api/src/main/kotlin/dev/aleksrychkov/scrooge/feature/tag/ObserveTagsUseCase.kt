package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.common.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface ObserveTagsUseCase {
    suspend operator fun invoke(): Result<Flow<ImmutableList<TagEntity>>>
}
