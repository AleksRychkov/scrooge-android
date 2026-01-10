package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import dev.aleksrychkov.scrooge.core.resources.categoryIconFromId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class TransactionsListMapper {

    fun transactionToUiItem(
        entity: TransactionEntity,
        date: String,
    ): TransactionsItem.Item {
        val sign = when (entity.type) {
            TransactionType.Income -> "+"
            TransactionType.Expense -> "-"
        }

        return TransactionsItem.Item(
            categoryName = entity.category.name,
            categoryIcon = categoryIconFromId(entity.category.iconId),
            categoryColor = entity.category.color,
            amount = "${entity.amount.amountToStringFormatted(sign)} " +
                entity.currency.currencySymbol,
            type = entity.type,
            tags = entity.tags.joinToString { it.name },
            id = entity.id,
            date = date,
        )
    }

    fun calculateTotals(transactions: List<TransactionEntity>): ImmutableList<String> {
        return transactions
            .groupBy {
                it.currency
            }
            .map { entry ->
                val total =
                    entry.value.sumOf { it.amount * (if (it.type == TransactionType.Expense) -1 else 1) }
                val sign = if (total > 0) "+" else "-"
                "${total.amountToStringFormatted(sign)} ${entry.key.currencySymbol}"
            }
            .toImmutableList()
    }
}
