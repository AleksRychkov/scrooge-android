package dev.aleksrychkov.scrooge.presentation.component.transactionlist

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.DefaultTransactionsListComponent

interface TransactionsListComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
        ): TransactionsListComponent {
            return DefaultTransactionsListComponent(
                componentContext = componentContext,
            )
        }
    }

    fun setFilters(filter: FilterEntity)
}
