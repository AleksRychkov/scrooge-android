package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity

fun interface GetTransactionUseCase {
    suspend operator fun invoke(id: Long): GetTransactionResult
}

sealed interface GetTransactionResult {
    data class Success(val transaction: TransactionEntity) : GetTransactionResult
    data object NotFound : GetTransactionResult
    data object Failure : GetTransactionResult
}
