package dev.aleksrychkov.scrooge.core.database.internal.dao

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.mapper.ReportMapper
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
) : ReportDao {

    private val database: Scrooge
        get() = db.value

    override suspend fun totalAmount(
        fromTimestamp: Long,
        toTimestamp: Long
    ): ReportTotalAmountEntity = withContext(readDispatcher) {
        val res = database.reportQueries
            .totalAmount(
                timestamp = fromTimestamp,
                timestamp_ = toTimestamp
            )
            .executeAsList()
        ReportMapper.totalAmountToEntity(res)
    }

    override suspend fun totalAmountMonthly(
        year: Int,
    ): ReportTotalAmountMonthlyEntity = withContext(readDispatcher) {
        val res = database.reportQueries
            .totalAmountMothly(value_ = year.toString())
            .executeAsList()
        ReportMapper.totalAmountMonthlyToEntity(res)
    }
}
