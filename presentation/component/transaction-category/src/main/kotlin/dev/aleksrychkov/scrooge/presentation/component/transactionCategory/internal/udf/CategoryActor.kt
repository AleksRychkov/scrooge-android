package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.DeleteCategoryDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.ObserveCategoryDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.SearchCategoryDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors.SwapOrderIndexDelegate
import kotlinx.coroutines.flow.Flow

internal class CategoryActor(
    private val deleteCategory: DeleteCategoryDelegate = DeleteCategoryDelegate(),
    private val searchCategory: SearchCategoryDelegate = SearchCategoryDelegate(),
    private val observeCategory: ObserveCategoryDelegate = ObserveCategoryDelegate(),
    private val swapDelegate: SwapOrderIndexDelegate = SwapOrderIndexDelegate(),
) : Actor<CategoryCommand, CategoryEvent> {

    override suspend fun process(command: CategoryCommand): Flow<CategoryEvent> {
        return when (command) {
            is CategoryCommand.ObserveCategories -> observeCategory(command)
            is CategoryCommand.Delete -> deleteCategory(command)
            is CategoryCommand.Search -> searchCategory(command)
            is CategoryCommand.SwapOrderIndex -> swapDelegate(command)
        }
    }
}
