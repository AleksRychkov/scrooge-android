package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryResult
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class RestoreCategoryDelegate(
    private val restoreCategoryUseCase: Lazy<RestoreCategoryUseCase>,
) {
    suspend operator fun invoke(cmd: CategoryCommand.Restore): Flow<CategoryEvent> {
        val result = restoreCategoryUseCase.value.invoke(categoryEntity = cmd.category)
        return when (result) {
            RestoreCategoryResult.Success -> emptyFlow()
            else -> flowOf(CategoryEvent.Internal.FailedToRestoreCategory)
        }
    }
}
