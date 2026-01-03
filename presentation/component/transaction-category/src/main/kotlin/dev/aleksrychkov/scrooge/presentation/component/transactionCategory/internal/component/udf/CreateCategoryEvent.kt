package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon

internal sealed interface CreateCategoryEvent {
    sealed interface External : CreateCategoryEvent {
        data object Init : CreateCategoryEvent
        data class SetName(val name: String) : CreateCategoryEvent
        data class SetIcon(val icon: CategoryIcon) : CreateCategoryEvent
        data class SetColor(val color: Int) : CreateCategoryEvent
        data object Submit : CreateCategoryEvent
    }

    sealed interface Internal : CreateCategoryEvent {
        data class DuplicateError(val duplicate: CategoryEntity) : CreateCategoryEvent
        data object EmptyNameError : CreateCategoryEvent
        data class SuccessLoad(val entity: CategoryEntity) : CreateCategoryEvent
        data object FailedLoad : CreateCategoryEvent
        data object SuccessSubmit : CreateCategoryEvent
        data object FailureSubmit : CreateCategoryEvent
    }
}
