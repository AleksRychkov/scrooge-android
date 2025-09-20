package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.common.database.TagDao
import dev.aleksrychkov.scrooge.common.entity.TagEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.tag.CreateTagResult
import dev.aleksrychkov.scrooge.feature.tag.CreateTagUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultCreateTagUseCase(
    private val tagDao: Lazy<TagDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : CreateTagUseCase {

    override suspend fun invoke(tagEntity: TagEntity): Result<CreateTagResult> =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val duplicateTag = tagDao.value.getByName(tagEntity.name)
                if (duplicateTag != null) {
                    CreateTagResult.DuplicateViolation(duplicateTag)
                } else {
                    tagDao.value.create(tagEntity.name, tagEntity.colorHex)
                    CreateTagResult.Success
                }
            }
        }
}
