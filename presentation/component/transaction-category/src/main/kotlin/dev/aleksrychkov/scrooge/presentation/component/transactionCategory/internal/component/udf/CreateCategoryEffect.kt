package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf

internal sealed interface CreateCategoryEffect {
    data class ShowInfoMessage(val message: String) : CreateCategoryEffect
}
