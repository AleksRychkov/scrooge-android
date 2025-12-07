package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import kotlinx.coroutines.flow.Flow

interface ReportDao {
    suspend fun amountForPeriodByTypeAndCodeEntity(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): Flow<ReportAmountForPeriodByTypeAndCodeEntity>
}
