package dev.aleksrychkov.scrooge.component.transactioncrud

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactioncrud.internal.DefaultTransactionCrudComponent
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionCrud

interface TransactionCrudComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            destination: DestinationTransactionCrud,
        ): TransactionCrudComponent {
            return DefaultTransactionCrudComponent(
                componentContext = componentContext,
                transactionId = destination.transactionId,
                transactionType = destination.transactionType,
            )
        }
    }
}
