package dev.aleksrychkov.scrooge.component.category.internal.udf

internal sealed interface CategoryEffect {
    data class ShowErrorMessage(val message: String) : CategoryEffect
}
