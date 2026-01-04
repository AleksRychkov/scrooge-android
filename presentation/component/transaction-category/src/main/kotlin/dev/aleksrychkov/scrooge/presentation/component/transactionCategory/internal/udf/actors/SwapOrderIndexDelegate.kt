package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.category.SwapOrderIndexCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class SwapOrderIndexDelegate(
    private val useCase: Lazy<SwapOrderIndexCategoryUseCase> = getLazy()
) {
    suspend operator fun invoke(cmd: CategoryCommand.SwapOrderIndex): Flow<CategoryEvent> {
        useCase.value.invoke(fromIndex = cmd.fromIndex, toIndex = cmd.toIndex, type = cmd.type)
        return emptyFlow()
    }
}
