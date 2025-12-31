package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf

internal sealed interface CategoryEffect {
    data class ShowInfoMessage(val message: String) : CategoryEffect
}
