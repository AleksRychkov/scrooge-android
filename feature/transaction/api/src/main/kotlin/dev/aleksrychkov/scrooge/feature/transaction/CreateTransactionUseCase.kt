package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity

fun interface CreateTransactionUseCase {
    suspend operator fun invoke(transaction: TransactionEntity): CreateTransactionResult
}

sealed interface CreateTransactionResult {
    data object Success : CreateTransactionResult
    data object Failure : CreateTransactionResult
}
