package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.mapper.ReportMapper
import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    ): Flow<ReportAmountForPeriodByTypeAndCodeEntity> = withContext(readDispatcher) {
        database.reportQueries
            .amountForPeriodByTypeAndCode(
                timestamp = fromTimestamp,
                timestamp_ = toTimestamp
            )
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                ReportMapper.amountForPeriodByTypeAndCodeToEntity(list)
            }
    }
}
