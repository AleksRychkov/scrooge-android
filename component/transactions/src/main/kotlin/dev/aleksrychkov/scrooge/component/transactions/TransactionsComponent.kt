package dev.aleksrychkov.scrooge.component.transactions

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactions.internal.DefaultTransactionsComponent

interface TransactionsComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): TransactionsComponent = DefaultTransactionsComponent(
            componentContext = componentContext,
        )
    }
}
