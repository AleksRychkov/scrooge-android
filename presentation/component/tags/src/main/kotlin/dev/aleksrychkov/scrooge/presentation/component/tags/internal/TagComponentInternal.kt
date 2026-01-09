package dev.aleksrychkov.scrooge.presentation.component.tags.internal

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.tags.TagComponent
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.TagEffect
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.TagState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface TagComponentInternal : TagComponent {
    val state: StateFlow<TagState>
    val effects: Flow<TagEffect>

    fun deleteTag(tag: TagEntity)
    fun setSearchQuery(query: String)
    fun addNewTag()
}
