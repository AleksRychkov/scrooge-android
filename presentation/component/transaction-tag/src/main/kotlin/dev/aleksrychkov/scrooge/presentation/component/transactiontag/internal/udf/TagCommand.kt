package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TagEntity

internal sealed interface TagCommand {
    data object ObserveTags : TagCommand
    data class Delete(val tag: TagEntity) : TagCommand
    data class Restore(val tag: TagEntity) : TagCommand
    data class Search(
        val query: String,
        val tags: List<TagEntity>,
    ) : TagCommand

    data class CreateNewTag(val name: String) : TagCommand
}
