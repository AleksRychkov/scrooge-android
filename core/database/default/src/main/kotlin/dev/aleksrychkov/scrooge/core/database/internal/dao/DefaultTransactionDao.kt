package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.database.internal.mapper.TransactionMapper
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultTransactionDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : TransactionDao {

    private val database: Scrooge
        get() = db.value

    override suspend fun get(
        filter: FilterEntity,
    ): Flow<ImmutableList<TransactionEntity>> = withContext(readDispatcher) {
        database.transactionQueries
            .selectFromTo(
                datestamp = filter.period.from.value,
                datestamp_ = filter.period.to.value,
            )
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                list.map(TransactionMapper::toEntity).toImmutableList()
            }
    }

    override suspend fun get(id: Long): TransactionEntity? = withContext(readDispatcher) {
        database.transactionQueries
            .selectById(id)
            .executeAsOneOrNull()
            ?.let(TransactionMapper::toEntity)
    }

    override suspend fun create(
        amount: Long,
        datestamp: Datestamp,
        type: TransactionType,
        category: String,
        tags: Set<String>?,
        currencyCode: String,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.create(
            amount = amount,
            datestamp = datestamp.value,
            type = type.type.toLong(),
            category = category,
            tags = TransactionMapper.toDatabaseTags(tags),
            currencyCode = currencyCode,
        )
    }

    override suspend fun update(
        id: Long,
        amount: Long,
        datestamp: Datestamp,
        type: TransactionType,
        category: String,
        tags: Set<String>?,
        currencyCode: String,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.update(
            amount = amount,
            datestamp = datestamp.value,
            type = type.type.toLong(),
            category = category,
            tags = TransactionMapper.toDatabaseTags(tags),
            currencyCode = currencyCode,
            id = id,
        )
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

    override suspend fun getAllTags(): ImmutableSet<String> = withContext(readDispatcher) {
        database.transactionQueries.allTags()
            .executeAsList()
            .map { it.tags }
            .map(TransactionMapper::toTags)
            .flatMap { it }
            .toImmutableSet()
    }
}
