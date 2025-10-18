package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultObserveCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultCategories: DefaultCategories,
) : ObserveCategoryUseCase {
    override suspend fun invoke(
        transactionType: TransactionType,
    ): ObserveCategoryResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val categories = categoryDao.value.get(transactionType)
                    .map {
                        (defaultCategories.get(transactionType) + it).toImmutableList()
                    }
                ObserveCategoryResult.Success(categories = categories)
            }.getOrDefault(ObserveCategoryResult.Failure)
        }
}
