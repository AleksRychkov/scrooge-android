package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsDeleteResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsDeleteUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultLimitsDeleteUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsDeleteUseCase {
    override suspend fun invoke(id: Long): LimitsDeleteResult = withContext(ioDispatcher) {
        runSuspendCatching {
            dao.value.delete(id = id)
            LimitsDeleteResult.Success
        }.getOrDefault(LimitsDeleteResult.Failure)
    }
}
