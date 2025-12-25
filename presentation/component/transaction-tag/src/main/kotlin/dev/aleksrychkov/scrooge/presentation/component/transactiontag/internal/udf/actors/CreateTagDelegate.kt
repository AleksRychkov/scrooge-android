package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.CreateTagResult
import dev.aleksrychkov.scrooge.feature.tag.CreateTagUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class CreateTagDelegate(
    private val createTagUseCase: Lazy<CreateTagUseCase>,
) {
    private val createMutex: Mutex by lazy { Mutex() }

    suspend operator fun invoke(cmd: TagCommand.CreateNewTag): Flow<TagEvent> {
        if (cmd.name.isBlank()) {
            return flowOf(TagEvent.Internal.FailedToCreateNewTagEmptyName)
        }

        return createMutex.withLock {
            val entity = TagEntity.from(name = cmd.name)
            val result = createTagUseCase.value.invoke(entity)
            when (result) {
                is CreateTagResult.DuplicateViolation -> {
                    flowOf(
                        TagEvent.Internal.FailedToCreateNewTagDuplicate(
                            tag = result.tag,
                        )
                    )
                }

                CreateTagResult.Failure -> {
                    flowOf(TagEvent.Internal.FailedToCreateNewTag)
                }

                CreateTagResult.Success -> {
                    emptyFlow()
                }
            }
        }
    }
}
