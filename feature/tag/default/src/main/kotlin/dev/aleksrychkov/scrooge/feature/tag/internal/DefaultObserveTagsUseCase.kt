package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.common.database.TagDao
import dev.aleksrychkov.scrooge.common.entity.TagEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class DefaultObserveTagsUseCase(
    private val tagDao: Lazy<TagDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ObserveTagsUseCase {

    override suspend fun invoke(): Result<Flow<ImmutableList<TagEntity>>> =
        withContext(ioDispatcher) {
            runSuspendCatching {
                tagDao.value.get()
            }
        }
}
