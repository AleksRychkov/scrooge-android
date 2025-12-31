package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

fun interface GetTransactionUseCase {
    suspend operator fun invoke(id: Long): GetTransactionResult
}

sealed interface GetTransactionResult {
    data class Success(val transactionFlow: Flow<TransactionEntity?>) : GetTransactionResult
    data object Failure : GetTransactionResult
}
