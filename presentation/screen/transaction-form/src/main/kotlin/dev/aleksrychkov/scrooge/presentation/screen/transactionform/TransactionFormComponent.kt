package dev.aleksrychkov.scrooge.presentation.screen.transactionform

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.DefaultTransactionFormComponent

interface TransactionFormComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext,
            destination: DestinationTransactionForm,
        ): TransactionFormComponent {
            return DefaultTransactionFormComponent(
                componentContext = componentContext,
                transactionId = destination.transactionId,
                transactionType = destination.transactionType,
            )
        }
    }
}
