package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

fun interface CreateTransactionUseCase {

    suspend operator fun invoke(args: Args): CreateTransactionResult

    data class Args(
        val amount: Long,
        val transactionType: TransactionType,
        val category: CategoryEntity,
        val tags: Set<TagEntity>? = null,
        val currency: CurrencyEntity,
        val timestamp: Long,
    )
}

sealed interface CreateTransactionResult {
    data object Success : CreateTransactionResult
    data object Failure : CreateTransactionResult
}
