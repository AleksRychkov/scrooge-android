package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.category.PreloadCategoriesUseCase
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListCommand
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class PreloadCategoriesDelegate(
    private val useCase: Lazy<PreloadCategoriesUseCase> = getLazy(),
) {
    suspend operator fun invoke(
        cmd: TransactionsListCommand.PreloadCategories,
    ): Flow<TransactionsListEvent> {
        useCase.value.invoke()
        return emptyFlow()
    }
}
