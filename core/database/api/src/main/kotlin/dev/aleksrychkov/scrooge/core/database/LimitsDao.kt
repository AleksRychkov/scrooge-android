package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface LimitsDao {
    suspend fun observe(): Flow<ImmutableList<LimitEntity>>
    suspend fun create(amount: Long, periodType: Int, currencyCode: String)
    suspend fun update(id: Long, amount: Long, periodType: Int, currencyCode: String)
    suspend fun delete(id: Long)
}
