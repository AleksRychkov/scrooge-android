package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors

import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SearchTagsDelegate {
    operator fun invoke(cmd: TagCommand.Search): Flow<TagEvent> {
        val filtered = when {
            cmd.query.isBlank() -> persistentListOf()
            else ->
                cmd.tags
                    .filter { it.name.lowercase().contains(cmd.query.lowercase()) }
                    .toImmutableList()
        }
        return flowOf(TagEvent.Internal.Filtered(list = filtered))
    }
}
