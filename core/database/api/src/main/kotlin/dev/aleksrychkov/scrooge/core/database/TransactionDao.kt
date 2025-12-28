package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface TransactionDao {

    suspend fun get(
        filter: FilterEntity,
    ): Flow<ImmutableList<TransactionEntity>>

    suspend fun get(id: Long): TransactionEntity?

    suspend fun create(
        amount: Long,
        timestamp: Long,
        type: TransactionType,
        category: String,
        tags: Set<String>?,
        currencyCode: String,
    )

    suspend fun update(
        id: Long,
        amount: Long,
        timestamp: Long,
        type: TransactionType,
        category: String,
        tags: Set<String>?,
        currencyCode: String,
    )

    suspend fun delete(id: Long)

    suspend fun getMinMaxTimestamp(): PeriodTimestampEntity?
}
