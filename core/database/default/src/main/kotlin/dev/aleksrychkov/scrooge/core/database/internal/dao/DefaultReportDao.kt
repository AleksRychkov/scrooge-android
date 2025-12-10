package dev.aleksrychkov.scrooge.core.database.internal.dao

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.mapper.ReportMapper
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
) : ReportDao {

    private val database: Scrooge
        get() = db.value

    override suspend fun amountForPeriodByTypeAndCodeEntity(
        fromTimestamp: Long,
        toTimestamp: Long
    ): ReportTotalAmountEntity = withContext(readDispatcher) {
        val res = database.reportQueries
            .amountForPeriodByTypeAndCode(
                timestamp = fromTimestamp,
                timestamp_ = toTimestamp
            )
            .executeAsList()
        ReportMapper.amountForPeriodByTypeAndCodeToEntity(res)
    }
}
