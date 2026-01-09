package dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.actors.CreateTagDelegate
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.actors.DeleteTagDelegate
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.actors.ObserveTagsDelegate
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.actors.SearchTagsDelegate
import kotlinx.coroutines.flow.Flow

internal class TagActor(
    private val createTag: CreateTagDelegate = CreateTagDelegate(),
    private val deleteTag: DeleteTagDelegate = DeleteTagDelegate(),
    private val observeTags: ObserveTagsDelegate = ObserveTagsDelegate(),
    private val searchTags: SearchTagsDelegate = SearchTagsDelegate(),
) : Actor<TagCommand, TagEvent> {
    override suspend fun process(command: TagCommand): Flow<TagEvent> {
        return when (command) {
            is TagCommand.Create -> createTag(command)
            is TagCommand.Delete -> deleteTag(command)
            TagCommand.Observe -> observeTags()
            is TagCommand.Search -> searchTags(command)
        }
    }
}
