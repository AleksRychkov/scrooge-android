package dev.aleksrychkov.scrooge.feature.category.internal

import android.content.Context
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicLong
import dev.aleksrychkov.scrooge.core.resources.R as Resources

class DefaultCategories(
    private val context: Context,
) {

    private val id: AtomicLong = AtomicLong(-1)
    private val mutex: Mutex by lazy { Mutex() }
    private var incomeCategories: List<CategoryEntity>? = null
    private var expenseCategories: List<CategoryEntity>? = null

    suspend fun get(transactionType: TransactionType): List<CategoryEntity> {
        return mutex.withLock {
            when (transactionType) {
                TransactionType.Income -> getIncomeCategories()
                TransactionType.Expense -> getExpenseCategories()
            }
        }
    }

    private fun getIncomeCategories(): List<CategoryEntity> {
        if (incomeCategories == null) {
            incomeCategories = context.resources.getStringArray(Resources.array.income_categories)
                .toList()
                .map { name ->
                    CategoryEntity(
                        id = id.andDecrement,
                        name = name,
                        type = TransactionType.Income,
                        isUserMade = false,
                    )
                }
        }
        return requireNotNull(incomeCategories)
    }

    private fun getExpenseCategories(): List<CategoryEntity> {
        if (expenseCategories == null) {
            expenseCategories = context.resources.getStringArray(Resources.array.expense_categories)
                .toList()
                .map { name ->
                    CategoryEntity(
                        id = id.andDecrement,
                        name = name,
                        type = TransactionType.Expense,
                        isUserMade = false,
                    )
                }
        }
        return requireNotNull(expenseCategories)
    }
}
