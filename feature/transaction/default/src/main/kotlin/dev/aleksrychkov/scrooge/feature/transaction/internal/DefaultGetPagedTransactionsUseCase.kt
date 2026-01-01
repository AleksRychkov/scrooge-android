package dev.aleksrychkov.scrooge.feature.transaction.internal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.feature.transaction.GetPagedTransactionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

internal class DefaultGetPagedTransactionsUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetPagedTransactionsUseCase {

    private companion object {
        const val PAGE_SIZE = 20
    }

    override suspend fun invoke(filter: FilterEntity): Flow<PagingData<TransactionEntity>> {
        transactionDao.value.prepareTagFilter(filter)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                transactionDao.value.getPaged(filter)
            }
        ).flow.flowOn(ioDispatcher)
    }
}
