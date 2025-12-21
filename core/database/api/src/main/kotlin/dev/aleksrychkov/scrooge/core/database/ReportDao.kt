package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import kotlinx.coroutines.flow.Flow

interface ReportDao {
    suspend fun totalAmount(period: PeriodTimestampEntity): Flow<ReportTotalAmountEntity>
    suspend fun totalAmountMonthly(year: Int): ReportTotalAmountMonthlyEntity
    suspend fun byCategory(period: PeriodTimestampEntity): ReportByCategoryEntity
}
