package dev.aleksrychkov.scrooge.component.tag.internal

import dev.aleksrychkov.scrooge.component.tag.TagComponent
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEffect
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagState
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface TagComponentInternal : TagComponent {
    val state: StateFlow<TagState>
    val effects: Flow<TagEffect>

    fun deleteTag(tag: TagEntity)
    fun restoreTag(tag: TagEntity)
    fun setSearchQuery(query: String)
    fun addNewTag()
}
