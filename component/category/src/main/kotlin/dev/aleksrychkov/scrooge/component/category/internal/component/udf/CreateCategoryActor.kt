package dev.aleksrychkov.scrooge.component.category.internal.component.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class CreateCategoryActor(
    private val useCase: Lazy<CreateCategoryUseCase> = getLazy(),
) : Actor<CreateCategoryCommand, CreateCategoryEvent> {

    private val createCategoryMutex: Mutex by lazy { Mutex() }

    override suspend fun process(command: CreateCategoryCommand): Flow<CreateCategoryEvent> {
        return when (command) {
            is CreateCategoryCommand.Submit -> create(
                name = command.state.name,
                icon = command.state.selectedCategoryIcon,
                transactionType = command.state.transactionType,
            )
        }
    }

    @Suppress("UnusedParameter")
    private suspend fun create(
        name: String,
        icon: CategoryIcon,
        transactionType: TransactionType,
    ): Flow<CreateCategoryEvent> {
        if (name.isBlank()) {
            return flowOf(CreateCategoryEvent.Internal.EmptyNameError)
        }

        return createCategoryMutex.withLock {
            val entity = CategoryEntity.from(name = name, type = transactionType)
            when (val result = useCase.value.invoke(categoryEntity = entity)) {
                is CreateCategoryResult.DuplicateViolation -> {
                    flowOf(
                        CreateCategoryEvent.Internal.DuplicateError(
                            duplicate = result.categoryEntity
                        )
                    )
                }

                CreateCategoryResult.Failure -> {
                    flowOf(CreateCategoryEvent.Internal.Failure)
                }

                CreateCategoryResult.Success -> {
                    flowOf(CreateCategoryEvent.Internal.Success)
                }
            }
        }
    }
}
