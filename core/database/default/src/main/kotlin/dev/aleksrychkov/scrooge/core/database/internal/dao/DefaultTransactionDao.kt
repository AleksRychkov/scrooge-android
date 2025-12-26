package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.database.internal.mapper.TransactionMapper
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
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
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : TransactionDao {

    private val database: Scrooge
        get() = db.value

    override suspend fun get(
        period: PeriodTimestampEntity,
    ): Flow<ImmutableList<TransactionEntity>> = withContext(readDispatcher) {
        database.transactionQueries
            .selectFromTo(period.from, period.to)
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
        timestamp: Long,
        type: TransactionType,
        category: String,
        tags: Set<String>?,
        currencyCode: String,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.create(
            amount = amount,
            timestamp = timestamp,
            type = type.type.toLong(),
            category = category,
            tags = TransactionMapper.toDatabaseTags(tags),
            currencyCode = currencyCode,
        )
    }

    override suspend fun update(
        id: Long,
        amount: Long,
        timestamp: Long,
        type: TransactionType,
        category: String,
        tags: Set<String>?,
        currencyCode: String,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.transactionQueries.update(
            amount = amount,
            timestamp = timestamp,
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

    override suspend fun getMinMaxTimestamp(): PeriodTimestampEntity? {
        val minMax = database.transactionQueries.minMaxTimestamp().executeAsOneOrNull()
        if (minMax == null || minMax.minTimestamp == null || minMax.maxTimestamp == null) return null
        return PeriodTimestampEntity(from = minMax.minTimestamp, to = minMax.maxTimestamp)
    }
}
