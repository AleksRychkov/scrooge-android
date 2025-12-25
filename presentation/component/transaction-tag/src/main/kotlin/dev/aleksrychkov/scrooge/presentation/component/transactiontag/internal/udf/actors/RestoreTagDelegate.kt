package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCase
import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCaseResult
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class RestoreTagDelegate(
    private val restoreTagUseCase: Lazy<RestoreTagUseCase>,
) {
    suspend operator fun invoke(cmd: TagCommand.Restore): Flow<TagEvent> {
        val result = restoreTagUseCase.value.invoke(tag = cmd.tag)
        return when (result) {
            RestoreTagUseCaseResult.Failure -> flowOf(TagEvent.Internal.FailedToRestoreTag)
            RestoreTagUseCaseResult.Success -> emptyFlow()
        }
    }
}
