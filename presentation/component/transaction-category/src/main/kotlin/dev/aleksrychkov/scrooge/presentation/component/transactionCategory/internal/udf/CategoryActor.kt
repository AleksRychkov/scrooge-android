package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.DeleteCategoryDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.ObserveCategoryDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.RestoreCategoryDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.SearchCategoryDelegate
import kotlinx.coroutines.flow.Flow

internal class CategoryActor(
    private val deleteCategory: DeleteCategoryDelegate,
    private val searchCategory: SearchCategoryDelegate,
    private val observeCategory: ObserveCategoryDelegate,
    private val restoreCategory: RestoreCategoryDelegate,
) : Actor<CategoryCommand, CategoryEvent> {

    companion object {
        operator fun invoke(): CategoryActor {
            return CategoryActor(
                deleteCategory = DeleteCategoryDelegate(deleteCategoryUseCase = getLazy()),
                searchCategory = SearchCategoryDelegate(),
                observeCategory = ObserveCategoryDelegate(observeCategoryUseCase = getLazy()),
                restoreCategory = RestoreCategoryDelegate(restoreCategoryUseCase = getLazy()),
            )
        }
    }

    override suspend fun process(command: CategoryCommand): Flow<CategoryEvent> {
        return when (command) {
            is CategoryCommand.ObserveCategories -> observeCategory(command)
            is CategoryCommand.Delete -> deleteCategory(command)
            is CategoryCommand.Search -> searchCategory(command)
            is CategoryCommand.Restore -> restoreCategory(command)
        }
    }
}
