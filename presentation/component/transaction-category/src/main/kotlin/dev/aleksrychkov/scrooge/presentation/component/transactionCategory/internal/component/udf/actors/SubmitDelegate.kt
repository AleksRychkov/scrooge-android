package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryUseCase
import dev.aleksrychkov.scrooge.feature.category.UpdateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.UpdateCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryEvent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SubmitDelegate(
    private val createUseCase: Lazy<CreateCategoryUseCase> = getLazy(),
    private val updateUseCase: Lazy<UpdateCategoryUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: CreateCategoryCommand.Submit): Flow<CreateCategoryEvent> {
        return if (cmd.state.id == null) {
            create(state = cmd.state)
        } else {
            update(state = cmd.state)
        }
    }

    private suspend fun create(
        state: CreateCategoryState
    ): Flow<CreateCategoryEvent> {
        val name = state.name
        val icon = state.selectedCategoryIcon
        val color = state.selectedCategoryColor
        val transactionType = state.transactionType
        if (name.isBlank()) {
            return flowOf(CreateCategoryEvent.Internal.EmptyNameError)
        }
        val entity = CategoryEntity.from(
            name = name.trim(),
            type = transactionType,
            iconId = icon.id,
            color = color,
        )
        return when (val result = createUseCase.value.invoke(categoryEntity = entity)) {
            is CreateCategoryResult.DuplicateViolation -> {
                flowOf(
                    CreateCategoryEvent.Internal.DuplicateError(
                        duplicate = result.categoryEntity
                    )
                )
            }

            CreateCategoryResult.Failure -> {
                flowOf(CreateCategoryEvent.Internal.FailureSubmit)
            }

            CreateCategoryResult.Success -> {
                flowOf(CreateCategoryEvent.Internal.SuccessSubmit)
            }
        }
    }

    private suspend fun update(
        state: CreateCategoryState
    ): Flow<CreateCategoryEvent> {
        val name = state.name
        val icon = state.selectedCategoryIcon
        val color = state.selectedCategoryColor
        val transactionType = state.transactionType
        if (name.isBlank()) {
            return flowOf(CreateCategoryEvent.Internal.EmptyNameError)
        }

        val entity = CategoryEntity(
            id = state.id!!,
            name = name.trim(),
            type = transactionType,
            iconId = icon.id,
            color = color,
        )

        return when (val result = updateUseCase.value.invoke(categoryEntity = entity)) {
            is UpdateCategoryResult.DuplicateViolation -> {
                flowOf(
                    CreateCategoryEvent.Internal.DuplicateError(
                        duplicate = result.categoryEntity
                    )
                )
            }

            UpdateCategoryResult.Failure -> {
                flowOf(CreateCategoryEvent.Internal.FailureSubmit)
            }

            UpdateCategoryResult.Success -> {
                flowOf(CreateCategoryEvent.Internal.SuccessSubmit)
            }
        }
    }
}
