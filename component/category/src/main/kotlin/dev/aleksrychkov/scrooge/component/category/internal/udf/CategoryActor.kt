package dev.aleksrychkov.scrooge.component.category.internal.udf

import dev.aleksrychkov.scrooge.component.category.internal.udf.actors.CreateCategoryDelegate
import dev.aleksrychkov.scrooge.component.category.internal.udf.actors.DeleteCategoryDelegate
import dev.aleksrychkov.scrooge.component.category.internal.udf.actors.ObserveCategoryDelegate
import dev.aleksrychkov.scrooge.component.category.internal.udf.actors.SearchCategoryDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow

internal class CategoryActor(
    private val createCategory: CreateCategoryDelegate,
    private val deleteCategory: DeleteCategoryDelegate,
    private val searchCategory: SearchCategoryDelegate,
    private val observeCategory: ObserveCategoryDelegate,
) : Actor<CategoryCommand, CategoryEvent> {

    companion object {
        operator fun invoke(): CategoryActor {
            return CategoryActor(
                createCategory = CreateCategoryDelegate(createCategoryUseCase = getLazy()),
                deleteCategory = DeleteCategoryDelegate(deleteCategoryUseCase = getLazy()),
                searchCategory = SearchCategoryDelegate(),
                observeCategory = ObserveCategoryDelegate(observeCategoryUseCase = getLazy()),
            )
        }
    }

    override suspend fun process(command: CategoryCommand): Flow<CategoryEvent> {
        return when (command) {
            is CategoryCommand.ObserveCategories -> observeCategory(command)
            is CategoryCommand.Delete -> deleteCategory(command)
            is CategoryCommand.Search -> searchCategory(command)
            is CategoryCommand.CreateNewCategory -> createCategory(command)
        }
    }
}
