package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsGetResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsGetUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultLimitsObserveUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsGetUseCase {
    override suspend fun invoke(): LimitsGetResult = withContext(ioDispatcher) {
        runSuspendCatching {
            LimitsGetResult.Success(result = dao.value.get())
        }.getOrDefault(LimitsGetResult.Failure)
    }
}
