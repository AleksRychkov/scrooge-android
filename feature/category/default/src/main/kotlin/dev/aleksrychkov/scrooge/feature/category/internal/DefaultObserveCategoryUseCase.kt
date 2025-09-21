package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.common.database.CategoryDao
import dev.aleksrychkov.scrooge.common.entity.CategoryEntity
import dev.aleksrychkov.scrooge.common.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class DefaultObserveCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ObserveCategoryUseCase {
    override suspend fun invoke(
        transactionType: TransactionType,
    ): Result<Flow<ImmutableList<CategoryEntity>>> =
        withContext(ioDispatcher) {
            runSuspendCatching {
                categoryDao.value.get(transactionType)
            }
        }
}
