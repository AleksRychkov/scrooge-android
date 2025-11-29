package dev.aleksrychkov.scrooge.component.category.internal.component.udf

internal sealed interface CreateCategoryCommand {
    data class Submit(val state: CreateCategoryState) : CreateCategoryCommand
}
