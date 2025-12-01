package dev.aleksrychkov.scrooge.feature.category.internal

import android.content.Context
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.R as Resources

class DefaultCategories(
    private val context: Context,
) {

    fun get(transactionType: TransactionType): List<CategoryEntity> {
        return when (transactionType) {
            TransactionType.Income -> getIncomeCategories()
            TransactionType.Expense -> getExpenseCategories()
        }
    }

    private fun getIncomeCategories(): List<CategoryEntity> {
        val iconIds = context.resources
            .getStringArray(Resources.array.income_categories_icon_ids)
        val names = context.resources
            .getStringArray(Resources.array.income_categories)
        return names.mapIndexed { index, name ->
            CategoryEntity.from(
                name = name,
                type = TransactionType.Income,
                iconId = iconIds[index] ?: "",
            )
        }
    }

    private fun getExpenseCategories(): List<CategoryEntity> {
        val iconIds = context.resources
            .getStringArray(Resources.array.expense_categories_icon_ids)
        val names = context.resources
            .getStringArray(Resources.array.expense_categories)
        return names.mapIndexed { index, name ->
            CategoryEntity.from(
                name = name,
                type = TransactionType.Expense,
                iconId = iconIds[index] ?: "",
            )
        }
    }
}
