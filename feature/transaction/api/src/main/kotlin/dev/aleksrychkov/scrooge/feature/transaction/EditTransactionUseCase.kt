package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

fun interface EditTransactionUseCase {

    suspend operator fun invoke(args: Args): EditTransactionResult

    data class Args(
        val transactionId: Long,
        val amount: Long,
        val transactionType: TransactionType,
        val category: CategoryEntity,
        val tags: Set<TagEntity>? = null,
        val currency: CurrencyEntity,
        val timestamp: Long,
    )
}

sealed interface EditTransactionResult {
    data object Success : EditTransactionResult
    data object Failure : EditTransactionResult
}
