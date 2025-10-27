package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCase
import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultRestoreTagUseCase(
    private val tagDao: Lazy<TagDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : RestoreTagUseCase {
    override suspend fun invoke(
        tag: TagEntity,
    ): RestoreTagUseCaseResult = withContext(ioDispatcher) {
        runSuspendCatching {
            tagDao.value.restore(tag.id)
            RestoreTagUseCaseResult.Success
        }.getOrDefault(RestoreTagUseCaseResult.Failure)
    }
}
