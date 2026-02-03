package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.database.DatabaseProvider
import dev.aleksrychkov.scrooge.core.database.internal.mapper.LimitsMapper
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultLimitsDao(
    private val dbProvider: DatabaseProvider,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : LimitsDao {

    private val database: Scrooge
        get() = dbProvider.scrooge

    override suspend fun observe(): Flow<ImmutableList<LimitEntity>> = withContext(readDispatcher) {
        database.limitsQueries
            .selectAll(mapper = LimitsMapper::toEntity)
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.toImmutableList() }
    }

    override suspend fun create(
        amount: Long,
        periodType: Int,
        currencyCode: String
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.limitsQueries
            .create(amount = amount, type = periodType.toLong(), currencyCode = currencyCode)
    }

    override suspend fun update(
        id: Long,
        amount: Long,
        periodType: Int,
        currencyCode: String
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.limitsQueries
            .update(
                amount = amount,
                type = periodType.toLong(),
                currencyCode = currencyCode,
                id = id,
            )
    }

    override suspend fun delete(id: Long): Unit = withContext(writeDispatcher + NonCancellable) {
        database.limitsQueries.delete(id = id)
    }
}
