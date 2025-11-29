package dev.aleksrychkov.scrooge.component.category.internal.component.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon

internal sealed interface CreateCategoryEvent {
    sealed interface External : CreateCategoryEvent {
        data class Init(
            val name: String,
            val transactionType: TransactionType,
        ) : CreateCategoryEvent

        data class SetName(val name: String) : CreateCategoryEvent
        data class SetIcon(val icon: CategoryIcon) : CreateCategoryEvent
        data object Submit : CreateCategoryEvent
    }

    sealed interface Internal : CreateCategoryEvent {
        data class DuplicateError(val duplicate: CategoryEntity) : CreateCategoryEvent
        data object EmptyNameError : CreateCategoryEvent
        data object Success : CreateCategoryEvent
        data object Failure : CreateCategoryEvent
    }
}
