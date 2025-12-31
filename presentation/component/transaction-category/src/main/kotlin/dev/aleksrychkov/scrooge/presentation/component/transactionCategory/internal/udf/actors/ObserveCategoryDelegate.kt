package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.udf.CategoryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class ObserveCategoryDelegate(
    private val observeCategoryUseCase: Lazy<ObserveCategoryUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: CategoryCommand.ObserveCategories): Flow<CategoryEvent> {
        val result = observeCategoryUseCase.value(cmd.transactionType)
        return if (result is ObserveCategoryResult.Success) {
            result
                .categories
                .map { CategoryEvent.Internal.Categories(list = it) }
        } else {
            emptyFlow()
        }
    }
}
