package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsUpdateResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsUpdateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultLimitsUpdateUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsUpdateUseCase {
    override suspend fun invoke(
        entity: LimitEntity,
    ): LimitsUpdateResult = withContext(ioDispatcher) {
        runSuspendCatching {
            dao.value.update(
                id = entity.id,
                amount = entity.amount,
                periodType = entity.period.type,
                currencyCode = entity.currency.currencyCode,
            )
            LimitsUpdateResult.Success
        }.getOrDefault(LimitsUpdateResult.Failure)
    }
}
