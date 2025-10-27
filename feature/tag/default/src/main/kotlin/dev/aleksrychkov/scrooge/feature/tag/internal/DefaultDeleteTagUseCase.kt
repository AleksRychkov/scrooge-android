package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.tag.DeleteTagResult
import dev.aleksrychkov.scrooge.feature.tag.DeleteTagUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultDeleteTagUseCase(
    private val tagDao: Lazy<TagDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : DeleteTagUseCase {

    override suspend fun invoke(tagEntity: TagEntity): DeleteTagResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                tagDao.value.delete(tagEntity.id)
                DeleteTagResult.Success
            }.getOrDefault(DeleteTagResult.Failure)
        }
}
