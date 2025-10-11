package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultObserveCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultCategories: Lazy<DefaultCategories>,
) : ObserveCategoryUseCase {
    override suspend fun invoke(
        transactionType: TransactionType,
    ): Result<Flow<ImmutableList<CategoryEntity>>> =
        withContext(ioDispatcher) {
            runSuspendCatching {
                categoryDao.value.get(transactionType)
                    .map {
                        (defaultCategories.value.get(transactionType) + it).toImmutableList()
                    }
            }
        }
}
