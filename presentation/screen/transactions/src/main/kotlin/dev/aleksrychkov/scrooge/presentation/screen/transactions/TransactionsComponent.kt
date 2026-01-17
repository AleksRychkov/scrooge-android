package dev.aleksrychkov.scrooge.presentation.screen.transactions

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.screen.transactions.internal.DefaultTransactionsComponent

interface TransactionsComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext,
            filter: FilterEntity
        ): TransactionsComponent = DefaultTransactionsComponent(
            componentContext = componentContext,
            filter = filter,
        )
    }
}
