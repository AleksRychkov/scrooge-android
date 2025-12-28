package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import kotlinx.coroutines.flow.Flow

interface ReportDao {
    suspend fun totalAmount(filter: FilterEntity): Flow<ReportTotalAmountEntity>
    suspend fun totalAmountMonthly(filter: FilterEntity): ReportTotalAmountMonthlyEntity
    suspend fun byCategory(filter: FilterEntity): ReportByCategoryEntity
}
