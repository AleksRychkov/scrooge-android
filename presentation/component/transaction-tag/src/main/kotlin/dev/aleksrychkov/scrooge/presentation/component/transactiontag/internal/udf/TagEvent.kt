package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

internal sealed interface TagEvent {
    sealed interface External : TagEvent {
        data object Init : External
        data class Search(val query: String) : External
        data class Delete(val tag: TagEntity) : External
        data object AddNewTag : External
    }

    sealed interface Internal : TagEvent {
        data class Tags(val set: ImmutableSet<TagEntity>) : Internal
        data class Filtered(val list: ImmutableList<TagEntity>) : Internal
        data object FailedToCreateNewTag : Internal
        data class FailedToCreateNewTagDuplicate(val tag: TagEntity) : Internal
        data object FailedToCreateNewTagEmptyName : Internal
        data object FailedToDeleteTag : Internal
    }
}
