package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity

fun interface EditTransactionUseCase {
    suspend operator fun invoke(transaction: TransactionEntity): EditTransactionResult
}

sealed interface EditTransactionResult {
    data object Success : EditTransactionResult
    data object Failure : EditTransactionResult
}
