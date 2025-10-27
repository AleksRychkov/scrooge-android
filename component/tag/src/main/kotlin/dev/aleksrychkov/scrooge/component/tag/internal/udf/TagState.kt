package dev.aleksrychkov.scrooge.component.tag.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class TagState(
    val tags: ImmutableList<TagEntity> = persistentListOf(),
    val filtered: ImmutableList<TagEntity> = persistentListOf(),
    val searchQuery: String = "",
)
