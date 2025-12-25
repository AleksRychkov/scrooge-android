package dev.aleksrychkov.scrooge.presentation.screen.transaction

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.DefaultTransactionsComponent

interface TransactionsComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): TransactionsComponent = DefaultTransactionsComponent(
            componentContext = componentContext,
        )
    }
}
