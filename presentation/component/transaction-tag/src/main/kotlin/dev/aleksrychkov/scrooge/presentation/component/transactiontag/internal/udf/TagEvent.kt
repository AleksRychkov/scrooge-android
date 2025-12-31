package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList

internal sealed interface TagEvent {
    sealed interface External : TagEvent {
        data object Init : External
        data class Search(val query: String) : External
        data class Delete(val tag: TagEntity) : External
        data object AddNewTag : External
    }

    sealed interface Internal : TagEvent {
        data class Tags(val list: ImmutableList<TagEntity>) : Internal
        data object FailedToObserveTags : Internal
        data class Filtered(val list: ImmutableList<TagEntity>) : Internal
        data object FailedToCreateNewTag : Internal
        data class FailedToCreateNewTagDuplicate(val tag: TagEntity) : Internal
        data object FailedToCreateNewTagEmptyName : Internal
        data object FailedToDeleteTag : Internal
    }
}
