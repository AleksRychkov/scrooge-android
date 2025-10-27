package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultObserveTagsUseCase(
    private val tagDao: Lazy<TagDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ObserveTagsUseCase {

    override suspend fun invoke(): ObserveTagsUseCaseResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                tagDao.value.get().let {
                    ObserveTagsUseCaseResult.Success(it)
                }
            }.getOrDefault(ObserveTagsUseCaseResult.Failure)
        }
}
