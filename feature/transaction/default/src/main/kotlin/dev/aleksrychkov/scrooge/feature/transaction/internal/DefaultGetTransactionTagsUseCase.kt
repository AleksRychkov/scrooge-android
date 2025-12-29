package dev.aleksrychkov.scrooge.feature.transaction.internal

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionTagsUseCase
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultGetTransactionTagsUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : GetTransactionTagsUseCase {
    override suspend fun invoke(): ImmutableSet<String> = withContext(ioDispatcher) {
        runSuspendCatching {
            transactionDao.value.getAllTags()
        }.getOrDefault(persistentSetOf())
    }
}
