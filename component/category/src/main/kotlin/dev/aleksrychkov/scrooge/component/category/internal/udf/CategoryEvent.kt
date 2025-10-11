package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList

internal sealed interface CategoryEvent {
    sealed interface External : CategoryEvent {
        data class Init(val transactionType: TransactionType) : External
        data class Search(val query: String) : External
        data class Delete(val categoryId: Long) : External
    }

    sealed interface Internal : CategoryEvent {
        data class Categories(val list: ImmutableList<CategoryEntity>) : Internal
        data object FailedToObserveCategories : Internal
        data class Filtered(val list: ImmutableList<CategoryEntity>) : Internal
    }
}
