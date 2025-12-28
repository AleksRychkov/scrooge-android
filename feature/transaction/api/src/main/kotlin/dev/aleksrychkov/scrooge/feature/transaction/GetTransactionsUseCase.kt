package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface GetTransactionsUseCase {
    suspend operator fun invoke(filter: FilterEntity): GetTransactionsResult
}

sealed interface GetTransactionsResult {
    data class Success(val result: Flow<ImmutableList<TransactionEntity>>) : GetTransactionsResult
    data object Failure : GetTransactionsResult
}
