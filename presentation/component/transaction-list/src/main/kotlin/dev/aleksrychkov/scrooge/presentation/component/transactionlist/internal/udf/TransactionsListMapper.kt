package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.resources.categoryIconFromId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.minus
import kotlinx.datetime.number
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class TransactionsListMapper(
    private val resourceManager: ResourceManager = get(),
) {

    fun transactionsToDayTransactions(
        transactions: List<TransactionEntity>,
    ): ImmutableList<TransactionsGroupDto> {
        val today = Datestamp.now().toLocalDate()
        return transactions
            .groupBy { transaction ->
                transaction.datestamp.toLocalDate()
            }
            .map { entry ->
                val diff = today.minus(entry.key)
                val date = when {
                    diff.years == 0 && diff.months == 0 && diff.days == 0 ->
                        resourceManager.getString(Resources.string.today)

                    diff.years == 0 && diff.months == 0 && diff.days == 1 ->
                        resourceManager.getString(Resources.string.yesterday)

                    else -> "${entry.key.day}.${entry.key.month.number}.${entry.key.year}"
                }
                TransactionsGroupDto(
                    date = date,
                    totals = calculateTotals(entry.value),
                    transactions = transactionToItemDto(entry.value),
                )
            }
            .toImmutableList()
    }

    private fun transactionToItemDto(transactions: List<TransactionEntity>): ImmutableList<TransactionsItemDto> {
        return transactions
            .map { t ->
                val sign = when (t.type) {
                    TransactionType.Income -> "+"
                    TransactionType.Expense -> "-"
                }
                TransactionsItemDto(
                    categoryName = t.category.name,
                    categoryIcon = categoryIconFromId(t.category.iconId),
                    categoryColor = t.category.color,
                    amount = "${t.amount.amountToStringFormatted(sign)} ${t.currency.currencySymbol}",
                    type = t.type,
                    ref = t,
                    tags = t.tags.joinToString()
                )
            }
            .toImmutableList()
    }

    private fun calculateTotals(transactions: List<TransactionEntity>): ImmutableList<String> {
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
