package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface GetTransactionsUseCase {

    suspend operator fun invoke(args: Args): GetTransactionsResult

    data class Args(
        val fromTimestamp: Long,
        val toTimestamp: Long,
    )
}

sealed interface GetTransactionsResult {
    data class Success(val result: Flow<ImmutableList<TransactionEntity>>) : GetTransactionsResult
    data object Failure : GetTransactionsResult
}
