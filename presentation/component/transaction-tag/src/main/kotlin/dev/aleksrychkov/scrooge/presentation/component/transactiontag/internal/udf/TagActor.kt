package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors.CreateTagDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors.DeleteTagDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors.ObserveTagsDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors.SearchTagsDelegate
import kotlinx.coroutines.flow.Flow

internal class TagActor(
    private val createTag: CreateTagDelegate,
    private val deleteTag: DeleteTagDelegate,
    private val observeTags: ObserveTagsDelegate,
    private val searchTags: SearchTagsDelegate,
) : Actor<TagCommand, TagEvent> {

    companion object {
        operator fun invoke(): TagActor {
            return TagActor(
                createTag = CreateTagDelegate(createTagUseCase = getLazy()),
                deleteTag = DeleteTagDelegate(deleteTagUseCase = getLazy()),
                observeTags = ObserveTagsDelegate(observeTagsUseCase = getLazy()),
                searchTags = SearchTagsDelegate(),
            )
        }
    }

    override suspend fun process(command: TagCommand): Flow<TagEvent> {
        return when (command) {
            is TagCommand.Create -> createTag(command)
            is TagCommand.Delete -> deleteTag(command)
            TagCommand.Observe -> observeTags()
            is TagCommand.Search -> searchTags(command)
        }
    }
}
