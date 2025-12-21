package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface GetTransactionsUseCase {
    suspend operator fun invoke(period: PeriodTimestampEntity): GetTransactionsResult
}

sealed interface GetTransactionsResult {
    data class Success(val result: Flow<ImmutableList<TransactionEntity>>) : GetTransactionsResult
    data object Failure : GetTransactionsResult
}
