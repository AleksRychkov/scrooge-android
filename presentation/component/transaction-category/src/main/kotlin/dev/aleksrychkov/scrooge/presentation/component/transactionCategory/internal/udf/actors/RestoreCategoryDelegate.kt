package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryResult
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class RestoreCategoryDelegate(
    private val restoreCategoryUseCase: Lazy<RestoreCategoryUseCase>,
) {
    suspend operator fun invoke(cmd: CategoryCommand.Restore): Flow<CategoryEvent> {
        val result = restoreCategoryUseCase.value.invoke(category = cmd.category)
        return when (result) {
            RestoreCategoryResult.Success -> emptyFlow()
            else -> flowOf(CategoryEvent.Internal.FailedToRestoreCategory)
        }
    }
}
