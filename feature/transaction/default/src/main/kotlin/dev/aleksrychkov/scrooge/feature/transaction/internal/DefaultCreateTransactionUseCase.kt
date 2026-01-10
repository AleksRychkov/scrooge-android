package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultCreateTransactionUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : CreateTransactionUseCase {
    override suspend fun invoke(transaction: TransactionEntity): CreateTransactionResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.create(
                    amount = transaction.amount,
                    datestamp = transaction.datestamp,
                    type = transaction.type,
                    categoryId = transaction.category.id,
                    tagIds = transaction.tags.map { it.id }.toSet(),
                    currencyCode = transaction.currency.currencyCode,
                    comment = transaction.comment,
                )
                CreateTransactionResult.Success
            }.getOrDefault(CreateTransactionResult.Failure)
        }
}
