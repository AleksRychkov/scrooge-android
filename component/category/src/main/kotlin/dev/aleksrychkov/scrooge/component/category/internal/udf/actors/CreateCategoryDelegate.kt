package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class CreateCategoryDelegate(
    private val createCategoryUseCase: Lazy<CreateCategoryUseCase>,
) {
    private val createCategoryMutex: Mutex by lazy { Mutex() }

    suspend operator fun invoke(cmd: CategoryCommand.CreateNewCategory): Flow<CategoryEvent> {
        if (cmd.name.isBlank()) {
            return flowOf(CategoryEvent.Internal.FailedToCreateNewCategoryEmptyName)
        }

        return createCategoryMutex.withLock {
            val entity = CategoryEntity.from(name = cmd.name, type = cmd.transactionType)
            val result = createCategoryUseCase.value.invoke(categoryEntity = entity)
            when (result) {
                is CreateCategoryResult.DuplicateViolation -> {
                    flowOf(
                        CategoryEvent.Internal.FailedToCreateNewCategoryDuplicate(
                            duplicate = result.categoryEntity
                        )
                    )
                }

                CreateCategoryResult.Failure -> {
                    flowOf(CategoryEvent.Internal.FailedToCreateNewCategory)
                }

                CreateCategoryResult.Success -> {
                    emptyFlow()
                }
            }
        }
    }
}
