package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetMostUsedCurrencyUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetMostUsedCurrencyUseCase {
    override suspend fun invoke(filter: FilterEntity): GetMostUsedCurrencyResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                transactionDao.value.getMostUsedCurrency(filter = filter)
                    ?.let { GetMostUsedCurrencyResult.Success(currency = it) }
                    ?: GetMostUsedCurrencyResult.Empty
            }.getOrDefault(GetMostUsedCurrencyResult.Empty)
        }
}
