package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultEditTransactionUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : EditTransactionUseCase {
    override suspend fun invoke(transaction: TransactionEntity): EditTransactionResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.update(
                    id = transaction.id,
                    amount = transaction.amount,
                    datestamp = transaction.datestamp,
                    type = transaction.type,
                    categoryId = transaction.category.id,
                    tagIds = transaction.tags.map { it.id }.toSet(),
                    currencyCode = transaction.currency.currencyCode,
                    comment = transaction.comment,
                )
                EditTransactionResult.Success
            }.getOrDefault(EditTransactionResult.Failure)
        }
}
