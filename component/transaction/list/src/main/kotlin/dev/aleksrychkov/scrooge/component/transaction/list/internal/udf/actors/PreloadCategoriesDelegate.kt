package dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.TransactionsListCommand
import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.TransactionsListEvent
import dev.aleksrychkov.scrooge.feature.category.PreloadCategoriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class PreloadCategoriesDelegate(
    private val useCase: Lazy<PreloadCategoriesUseCase>
) {
    suspend operator fun invoke(
        cmd: TransactionsListCommand.PreloadCategories,
    ): Flow<TransactionsListEvent> {
        useCase.value.invoke()
        return emptyFlow()
    }
}
