package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultLimitsObserveUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsObserveUseCase {
    override suspend fun invoke(): LimitsObserveResult = withContext(ioDispatcher) {
        runSuspendCatching {
            val flow = dao.value.observe()
            LimitsObserveResult.Success(result = flow)
        }.getOrDefault(LimitsObserveResult.Failure)
    }
}
