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
        val colors = intArrayOf(
            0xFFC0CA33.toInt(),
            0xFF00897B.toInt(),
            0xFF87A861.toInt(),
        )
        return names.mapIndexed { index, name ->
            CategoryEntity.from(
                name = name,
                type = TransactionType.Income,
                iconId = iconIds[index] ?: "",
                color = colors[index],
            )
        }
    }

    private fun getExpenseCategories(): List<CategoryEntity> {
        val iconIds = context.resources
            .getStringArray(Resources.array.expense_categories_icon_ids)
        val names = context.resources
            .getStringArray(Resources.array.expense_categories)
        val colors = intArrayOf(
            0xFFFFB54B.toInt(),
            0xFFE53935.toInt(),
            0xFF5E35B1.toInt(),
            0xFFFFB300.toInt(),
            0xFF509CFF.toInt(),
            0xFFA6E88F.toInt(),
            0xFF9C27B0.toInt(),
            0xFFA0A0A0.toInt(),
        )
        return names.mapIndexed { index, name ->
            CategoryEntity.from(
                name = name,
                type = TransactionType.Expense,
                iconId = iconIds[index] ?: "",
                color = colors[index],
            )
        }
    }
}
