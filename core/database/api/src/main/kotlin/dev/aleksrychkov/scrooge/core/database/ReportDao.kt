package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity

interface ReportDao {
    suspend fun amountForPeriodByTypeAndCodeEntity(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): ReportTotalAmountEntity
}
