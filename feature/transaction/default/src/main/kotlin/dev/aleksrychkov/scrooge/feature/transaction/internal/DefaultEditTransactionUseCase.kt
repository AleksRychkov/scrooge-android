package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultEditTransactionUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : EditTransactionUseCase {
    override suspend fun invoke(args: EditTransactionUseCase.Args): EditTransactionResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.update(
                    id = args.transactionId,
                    amount = args.amount,
                    timestamp = args.timestamp,
                    type = args.transactionType,
                    category = args.category.name,
                    tags = args.tags?.map { it.name }?.toSet(),
                    currencyCode = args.currency.currencyCode,
                )
                EditTransactionResult.Success
            }.getOrDefault(EditTransactionResult.Failure)
        }
}
