package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.database.DatabaseProvider
import dev.aleksrychkov.scrooge.core.database.internal.mapper.IncomeExpenseTimelineMapper
import dev.aleksrychkov.scrooge.core.database.internal.mapper.ReportMapper
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.ReportCategoryTimelineEntity
import dev.aleksrychkov.scrooge.core.entity.ReportIncomeExpenseTimelineEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReportDao(
    private val dbProvider: DatabaseProvider,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : ReportDao {

    private val database: Scrooge
        get() = dbProvider.scrooge

    override suspend fun totalAmount(filter: FilterEntity): Flow<ReportTotalAmountEntity> =
        withContext(readDispatcher) {
            prepareTagFilter(filter)
            database.reportQueries
                .totalAmount(
                    fromDatestamp = filter.period.from.value,
                    toDatestamp = filter.period.to.value,
                    categoryId = filter.category?.id,
                    transactionType = filter.transactionType?.type?.toLong(),
                    currencyCode = filter.currency?.currencyCode,
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
                categoryId = filter.category?.id,
                transactionType = filter.transactionType?.type?.toLong(),
                currencyCode = filter.currency?.currencyCode,
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
                categoryId = filter.category?.id,
                transactionType = filter.transactionType?.type?.toLong(),
                currencyCode = filter.currency?.currencyCode,
            )
            .executeAsList()
            .let(ReportMapper::byCategoryToEntity)
    }

    override suspend fun balanceTotalTimeline(
        filter: FilterEntity,
    ): ReportBalanceTimelineEntity = withContext(readDispatcher) {
        database.reportQueries
            .balanceTotalTimeline(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
                currencyCode = filter.currency?.currencyCode,
            )
            .executeAsList()
            .let { rows ->
                ReportMapper.balanceTotalTimelineToEntity(
                    list = rows,
                    period = filter.period,
                )
            }
    }

    override suspend fun incomeExpenseTimeline(
        filter: FilterEntity,
    ): ReportIncomeExpenseTimelineEntity = withContext(readDispatcher) {
        database.reportQueries
            .incomeExpenseTimeline(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
                currencyCode = filter.currency?.currencyCode,
            )
            .executeAsList()
            .let { rows ->
                IncomeExpenseTimelineMapper.toEntity(
                    list = rows,
                    period = filter.period,
                )
            }
    }

    override suspend fun categoryTimeline(
        filter: FilterEntity,
    ): ReportCategoryTimelineEntity = withContext(readDispatcher) {
        prepareTagFilter(filter)
        database.reportQueries
            .categoryTimeline(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
                categoryId = filter.category?.id,
                transactionType = filter.transactionType?.type?.toLong(),
                currencyCode = filter.currency?.currencyCode,
            )
            .executeAsList()
            .let { rows ->
                ReportMapper.categoryTimelineToEntity(
                    list = rows,
                    period = filter.period,
                )
            }
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
