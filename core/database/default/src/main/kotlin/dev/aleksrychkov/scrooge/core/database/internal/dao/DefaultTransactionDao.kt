package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.database.internal.database.DatabaseProvider
import dev.aleksrychkov.scrooge.core.database.internal.mapper.TransactionMapper
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultTransactionDao(
    private val dbProvider: DatabaseProvider,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : TransactionDao {

    private val database: Scrooge
        get() = dbProvider.scrooge

    override suspend fun get(
        filter: FilterEntity,
    ): Flow<ImmutableList<TransactionEntity>> = withContext(readDispatcher) {
        prepareTagFilter(filter)
        database.transactionQueries
            .selectFromTo(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
                categoryId = filter.category?.id,
                transactionType = filter.transactionType?.type?.toLong(),
                mapper = TransactionMapper::transactionEntityMapper,
            )
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.toImmutableList() }
    }

    override suspend fun prepareTagFilter(filter: FilterEntity) =
        withContext(writeDispatcher + NonCancellable) {
            database.tagQueries.transaction {
                database.tagQueries.clearFilterTags()
                filter.tags.forEach {
                    database.tagQueries.insertFilterTag(it.id)
                }
            }
        }

    override fun getPaged(filter: FilterEntity): PagingSource<Long, TransactionEntity> {
        val boundariesQuery = { _: Long?, _: Long ->
            database.transactionQueries.selectFromToKeydBoundaries(
                fromDatestamp = filter.period.from.value,
                toDatestamp = filter.period.to.value,
            )
        }
        return QueryPagingSource(
            transacter = database.transactionQueries,
            context = readDispatcher,
            pageBoundariesProvider = boundariesQuery,
            queryProvider = { from, to ->
                database.transactionQueries
                    .selectFromToKeyd(
                        fromDatestamp = from,
                        toDatestamp = to ?: (filter.period.from.value - 1),
                        categoryId = filter.category?.id,
                        transactionType = filter.transactionType?.type?.toLong(),
                        currencyCode = filter.currency?.currencyCode,
                        mapper = TransactionMapper::transactionEntityMapper,
                    )
            }
        )
    }

    override suspend fun get(id: Long): Flow<TransactionEntity?> = withContext(readDispatcher) {
        database.transactionQueries
            .selectById(
                id = id,
                mapper = TransactionMapper::transactionEntityMapper,
            )
            .asFlow()
            .mapToOneOrNull(readDispatcher)
    }

    override suspend fun create(
        amount: Long,
        datestamp: Datestamp,
        type: TransactionType,
        categoryId: Long,
        tagIds: Set<Long>?,
        currencyCode: String,
        comment: String?,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.transaction {
            database.transactionQueries.create(
                amount = amount,
                datestamp = datestamp.value,
                type = type.type.toLong(),
                categoryId = categoryId,
                currencyCode = currencyCode,
                comment = comment,
            )
            val transactionId = database.transactionQueries.getLastInsertId().executeAsOne()
            tagIds?.forEach { tagId ->
                database.tagQueries.createTransactioTag(
                    transactionId = transactionId,
                    tagId = tagId,
                )
            }
        }
    }

    override suspend fun update(
        id: Long,
        amount: Long,
        datestamp: Datestamp,
        type: TransactionType,
        categoryId: Long,
        tagIds: Set<Long>?,
        currencyCode: String,
        comment: String?,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.transaction {
            database.transactionQueries.update(
                id = id,
                amount = amount,
                datestamp = datestamp.value,
                type = type.type.toLong(),
                categoryId = categoryId,
                currencyCode = currencyCode,
                comment = comment,
            )
            database.tagQueries.deleteTransactioTag(transactionId = id)
            tagIds?.forEach { tagId ->
                database.tagQueries.createTransactioTag(transactionId = id, tagId = tagId)
            }
        }
    }

    override suspend fun delete(id: Long): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.delete(id)
    }

    override suspend fun getMinMaxDatestamp(): PeriodDatestampEntity? =
        withContext(readDispatcher) {
            val minMax = database.transactionQueries.minMaxDatestamp().executeAsOneOrNull()
            if (minMax == null || minMax.minDatestamp == null || minMax.maxDatestamp == null) return@withContext null
            PeriodDatestampEntity(
                from = Datestamp(minMax.minDatestamp),
                to = Datestamp(minMax.maxDatestamp),
            )
        }
}
