package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import kotlinx.coroutines.flow.Flow

interface ReportDao {
    suspend fun totalAmount(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): Flow<ReportTotalAmountEntity>

    suspend fun totalAmountMonthly(
        year: Int
    ): ReportTotalAmountMonthlyEntity
}
