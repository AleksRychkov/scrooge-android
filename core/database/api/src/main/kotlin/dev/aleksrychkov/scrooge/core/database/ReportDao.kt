package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity

interface ReportDao {
    suspend fun totalAmount(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): ReportTotalAmountEntity

    suspend fun totalAmountMonthly(
        year: Int
    ): ReportTotalAmountMonthlyEntity
}
