package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

internal sealed interface CategoryCommand {
    data class ObserveCategories(val transactionType: TransactionType) : CategoryCommand
    data class Delete(val category: CategoryEntity) : CategoryCommand
    data class Search(val query: String, val categories: List<CategoryEntity>) : CategoryCommand
    data class SwapOrderIndex(val newOrder: List<Pair<Long, Int>>) : CategoryCommand
}
