package dev.aleksrychkov.scrooge.feature.transaction

import androidx.paging.PagingData
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

fun interface GetPagedTransactionsUseCase {
    suspend operator fun invoke(filter: FilterEntity): Flow<PagingData<TransactionEntity>>
}
