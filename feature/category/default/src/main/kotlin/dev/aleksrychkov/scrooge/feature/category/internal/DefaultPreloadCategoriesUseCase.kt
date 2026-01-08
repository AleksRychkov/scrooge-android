package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.PreloadCategoriesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultPreloadCategoriesUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultCategories: DefaultCategories,
) : PreloadCategoriesUseCase {
    override suspend fun invoke(): Unit = withContext(ioDispatcher) {
        runSuspendCatching {
            val isPreloadNeeded = categoryDao.value.isPreloadNeeded()
            if (!isPreloadNeeded) return@runSuspendCatching
            defaultCategories.get(TransactionType.Expense).forEach { entity ->
                categoryDao.value.create(
                    name = entity.name,
                    type = TransactionType.Expense,
                    iconId = entity.iconId,
                    color = entity.color,
                )
            }
            defaultCategories.get(TransactionType.Income).forEach { entity ->
                categoryDao.value.create(
                    name = entity.name,
                    type = TransactionType.Income,
                    iconId = entity.iconId,
                    color = entity.color,
                )
            }
        }
    }
}
