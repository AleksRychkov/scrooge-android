package dev.aleksrychkov.scrooge.feature.category.internal

import android.content.Context
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import java.util.concurrent.atomic.AtomicLong
import dev.aleksrychkov.scrooge.core.resources.R as Resources

class DefaultCategories(
    private val context: Context,
) {

    private val id: AtomicLong = AtomicLong(-1)

    fun get(transactionType: TransactionType): List<CategoryEntity> =
        when (transactionType) {
            TransactionType.Income -> getIncomeCategories()
            TransactionType.Expense -> getExpenseCategories()
        }

    private fun getIncomeCategories(): List<CategoryEntity> =
        context.resources.getStringArray(Resources.array.income_categories)
            .toList()
            .map { name ->
                CategoryEntity(
                    id = id.andDecrement,
                    name = name,
                    type = TransactionType.Income,
                    isUserMade = false,
                )
            }

    private fun getExpenseCategories(): List<CategoryEntity> =
        context.resources.getStringArray(Resources.array.expense_categories)
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
