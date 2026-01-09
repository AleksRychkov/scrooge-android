package dev.aleksrychkov.scrooge.presentation.component.category.internal.component.udf

internal sealed interface CreateCategoryEffect {
    data class ShowInfoMessage(val message: String) : CreateCategoryEffect
}
