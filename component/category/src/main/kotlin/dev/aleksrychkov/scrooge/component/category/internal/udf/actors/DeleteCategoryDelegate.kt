package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryResult
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class DeleteCategoryDelegate(
    private val deleteCategoryUseCase: Lazy<DeleteCategoryUseCase>,
) {
    suspend operator fun invoke(cmd: CategoryCommand.Delete): Flow<CategoryEvent> {
        val result = deleteCategoryUseCase.value.invoke(cmd.category)
        return when (result) {
            DeleteCategoryResult.Success -> emptyFlow()
            else -> flowOf(CategoryEvent.Internal.FailedToDeleteCategory)
        }
    }
}
