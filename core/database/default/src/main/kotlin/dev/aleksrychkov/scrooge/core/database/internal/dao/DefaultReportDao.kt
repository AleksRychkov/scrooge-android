package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.mapper.ReportMapper
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
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

    override suspend fun totalAmount(period: PeriodTimestampEntity): Flow<ReportTotalAmountEntity> =
        withContext(readDispatcher) {
            database.reportQueries
                .totalAmount(
                    timestamp = period.from,
                    timestamp_ = period.to,
                )
                .asFlow()
                .mapToList(readDispatcher)
                .map { list ->
                    ReportMapper.totalAmountToEntity(list)
                }
        }

    override suspend fun totalAmountMonthly(
        period: PeriodTimestampEntity,
    ): ReportTotalAmountMonthlyEntity = withContext(readDispatcher) {
        database.reportQueries
            .totalAmountMothly(
                timestamp = period.from,
                timestamp_ = period.to,
            )
            .executeAsList()
            .let(ReportMapper::totalAmountMonthlyToEntity)
    }

    override suspend fun byCategory(
        period: PeriodTimestampEntity,
    ): ReportByCategoryEntity = withContext(readDispatcher) {
        database.reportQueries
            .byCategory(
                timestamp = period.from,
                timestamp_ = period.to,
            )
            .executeAsList()
            .let(ReportMapper::byCategoryToEntity)
    }
}
