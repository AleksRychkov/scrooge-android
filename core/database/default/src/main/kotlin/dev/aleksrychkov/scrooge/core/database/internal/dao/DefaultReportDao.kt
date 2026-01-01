package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.mapper.ReportMapper
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReportDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : ReportDao {

    private val database: Scrooge by lazy { db.value }

    override suspend fun totalAmount(filter: FilterEntity): Flow<ReportTotalAmountEntity> =
        withContext(readDispatcher) {
            prepareTagFilter(filter)
            database.reportQueries
                .totalAmount(
                    fromDatestamp = filter.period.from.value,
                    toDatestamp = filter.period.to.value,
                )
                .asFlow()
                .mapToList(readDispatcher)
                .map { list ->
                    ReportMapper.totalAmountToEntity(list)
                }
        }

    override suspend fun totalAmountMonthly(
        filter: FilterEntity,
    ): ReportTotalAmountMonthlyEntity = withContext(readDispatcher) {
        prepareTagFilter(filter)
        database.reportQueries
            .totalAmountMothly(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
            )
            .executeAsList()
            .let(ReportMapper::totalAmountMonthlyToEntity)
    }

    override suspend fun byCategory(
        filter: FilterEntity,
    ): ReportByCategoryEntity = withContext(readDispatcher) {
        prepareTagFilter(filter)
        database.reportQueries
            .byCategory(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
            )
            .executeAsList()
            .let(ReportMapper::byCategoryToEntity)
    }

    private suspend fun prepareTagFilter(filter: FilterEntity) =
        withContext(writeDispatcher + NonCancellable) {
            database.tagQueries.transaction {
                database.tagQueries.clearFilterTags()
                filter.tags.forEach {
                    database.tagQueries.insertFilterTag(it.id)
                }
            }
        }
}
