package dev.aleksrychkov.scrooge.feature.transaction

fun interface DeleteTransactionUseCase {
    suspend operator fun invoke(transactionId: Long): DeleteTransactionResult
}

sealed interface DeleteTransactionResult {
    data object Success : DeleteTransactionResult
    data object Failure : DeleteTransactionResult
}
