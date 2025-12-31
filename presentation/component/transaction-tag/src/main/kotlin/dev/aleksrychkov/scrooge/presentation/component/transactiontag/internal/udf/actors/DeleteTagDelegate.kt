package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.tag.DeleteTagResult
import dev.aleksrychkov.scrooge.feature.tag.DeleteTagUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class DeleteTagDelegate(
    private val deleteTagUseCase: Lazy<DeleteTagUseCase>,
) {
    suspend operator fun invoke(cmd: TagCommand.Delete): Flow<TagEvent> {
        val result = deleteTagUseCase.value.invoke(cmd.tag)
        return when (result) {
            DeleteTagResult.Failure -> flowOf(TagEvent.Internal.FailedToDeleteTag)
            DeleteTagResult.Success -> emptyFlow()
        }
    }
}
