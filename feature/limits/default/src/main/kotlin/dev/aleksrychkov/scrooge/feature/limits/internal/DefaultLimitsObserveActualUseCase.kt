package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveActualResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveActualUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultLimitsObserveActualUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsObserveActualUseCase {
    override suspend fun invoke(): LimitsObserveActualResult = withContext(ioDispatcher) {
        runSuspendCatching {
            val limits = dao.value.observeActual()
            LimitsObserveActualResult.Success(limits = limits)
        }.getOrDefault(LimitsObserveActualResult.Failure)
    }
}
