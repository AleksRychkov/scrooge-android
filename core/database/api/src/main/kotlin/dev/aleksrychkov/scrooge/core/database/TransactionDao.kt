package dev.aleksrychkov.scrooge.core.database

import androidx.paging.PagingSource
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface TransactionDao {

    suspend fun get(filter: FilterEntity): Flow<ImmutableList<TransactionEntity>>

    suspend fun prepareTagFilter(filter: FilterEntity)

    fun getPaged(filter: FilterEntity): PagingSource<Long, TransactionEntity>

    suspend fun get(id: Long): Flow<TransactionEntity?>

    suspend fun create(
        amount: Long,
        datestamp: Datestamp,
        type: TransactionType,
        categoryId: Long,
        tagIds: Set<Long>?,
        currencyCode: String,
        comment: String?,
    )

    suspend fun update(
        id: Long,
        amount: Long,
        datestamp: Datestamp,
        type: TransactionType,
        categoryId: Long,
        tagIds: Set<Long>?,
        currencyCode: String,
        comment: String?,
    )

    suspend fun delete(id: Long)

    suspend fun getMinMaxDatestamp(): PeriodDatestampEntity?
}
