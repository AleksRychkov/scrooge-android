package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.database.CategoryDao.CategoryOrder
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.category.SwapOrderIndexCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultSwapOrderIndexCategoryUseCase(
    private val categoryDao: Lazy<CategoryDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : SwapOrderIndexCategoryUseCase {
    override suspend fun invoke(args: SwapOrderIndexCategoryUseCase.Args): Unit =
        withContext(ioDispatcher) {
            runSuspendCatching {
                categoryDao.value.updateOrderIndex(
                    list = args.data.map {
                        CategoryOrder(
                            id = it.categoryId,
                            orderIndex = it.orderIndex
                        )
                    },
                )
            }
        }
}
