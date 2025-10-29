package dev.aleksrychkov.scrooge.component.transactionform

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactionform.internal.DefaultTransactionFormComponent
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm

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
