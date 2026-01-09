package dev.aleksrychkov.scrooge.presentation.component.category.internal.component.udf

internal sealed interface CreateCategoryCommand {
    data class Submit(val state: CreateCategoryState) : CreateCategoryCommand
    data class Load(val id: Long) : CreateCategoryCommand
}
