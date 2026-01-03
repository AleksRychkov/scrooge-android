package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.category.GetCategoryResult
import dev.aleksrychkov.scrooge.feature.category.GetCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadDelegate(
    private val useCase: Lazy<GetCategoryUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: CreateCategoryCommand.Load): Flow<CreateCategoryEvent> {
        return when (val res = useCase.value.invoke(id = cmd.id)) {
            is GetCategoryResult.Success -> flowOf(CreateCategoryEvent.Internal.SuccessLoad(entity = res.entity))
            else -> flowOf(CreateCategoryEvent.Internal.FailedLoad)
        }
    }
}
