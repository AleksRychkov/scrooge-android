package dev.aleksrychkov.scrooge.component.tag.internal.udf

import dev.aleksrychkov.scrooge.component.tag.internal.udf.actors.CreateTagDelegate
import dev.aleksrychkov.scrooge.component.tag.internal.udf.actors.DeleteTagDelegate
import dev.aleksrychkov.scrooge.component.tag.internal.udf.actors.ObserveTagsDelegate
import dev.aleksrychkov.scrooge.component.tag.internal.udf.actors.RestoreTagDelegate
import dev.aleksrychkov.scrooge.component.tag.internal.udf.actors.SearchTagsDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow

internal class TagActor(
    private val createTag: CreateTagDelegate,
    private val deleteTag: DeleteTagDelegate,
    private val observeTags: ObserveTagsDelegate,
    private val searchTags: SearchTagsDelegate,
    private val restoreTag: RestoreTagDelegate,
) : Actor<TagCommand, TagEvent> {

    companion object {
        operator fun invoke(): TagActor {
            return TagActor(
                createTag = CreateTagDelegate(createTagUseCase = getLazy()),
                deleteTag = DeleteTagDelegate(deleteTagUseCase = getLazy()),
                observeTags = ObserveTagsDelegate(observeTagsUseCase = getLazy()),
                searchTags = SearchTagsDelegate(),
                restoreTag = RestoreTagDelegate(restoreTagUseCase = getLazy()),
            )
        }
    }

    override suspend fun process(command: TagCommand): Flow<TagEvent> {
        return when (command) {
            is TagCommand.CreateNewTag -> createTag(command)
            is TagCommand.Delete -> deleteTag(command)
            TagCommand.ObserveTags -> observeTags()
            is TagCommand.Restore -> restoreTag(command)
            is TagCommand.Search -> searchTags(command)
        }
    }
}
