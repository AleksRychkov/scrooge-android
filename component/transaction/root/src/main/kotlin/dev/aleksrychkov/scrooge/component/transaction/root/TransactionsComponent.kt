package dev.aleksrychkov.scrooge.component.transaction.root

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transaction.root.internal.DefaultTransactionsComponent

interface TransactionsComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): TransactionsComponent = DefaultTransactionsComponent(
            componentContext = componentContext,
        )
    }
}
