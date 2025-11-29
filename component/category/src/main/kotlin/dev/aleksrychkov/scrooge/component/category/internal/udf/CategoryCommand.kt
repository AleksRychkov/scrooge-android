package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType

internal sealed interface CategoryCommand {
    data class ObserveCategories(val transactionType: TransactionType) : CategoryCommand
    data class Delete(val category: CategoryEntity) : CategoryCommand
    data class Restore(val category: CategoryEntity) : CategoryCommand
    data class Search(
        val query: String,
        val categories: List<CategoryEntity>,
    ) : CategoryCommand
}
