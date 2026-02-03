package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsCreateResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsCreateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultLimitsCreateUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsCreateUseCase {
    override suspend fun invoke(
        entity: LimitEntity,
    ): LimitsCreateResult = withContext(ioDispatcher) {
        runSuspendCatching {
            dao.value.create(
                amount = entity.amount,
                periodType = entity.period.type,
                currencyCode = entity.currency.currencyCode,
            )
            LimitsCreateResult.Success
        }.getOrDefault(LimitsCreateResult.Failure)
    }
}
