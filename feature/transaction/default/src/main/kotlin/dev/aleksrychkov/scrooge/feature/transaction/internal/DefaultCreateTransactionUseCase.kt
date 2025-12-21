package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultCreateTransactionUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : CreateTransactionUseCase {
    override suspend fun invoke(args: CreateTransactionUseCase.Args): CreateTransactionResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.create(
                    amount = args.amount,
                    timestamp = args.timestamp,
                    type = args.transactionType,
                    category = args.category.name,
                    tags = args.tags?.map { it.name }?.toSet(),
                    currencyCode = args.currency.currencyCode,
                )
                CreateTransactionResult.Success
            }.getOrDefault(CreateTransactionResult.Failure)
        }
}
