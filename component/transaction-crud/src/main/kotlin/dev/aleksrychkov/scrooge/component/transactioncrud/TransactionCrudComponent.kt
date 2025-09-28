package dev.aleksrychkov.scrooge.component.transactioncrud

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactioncrud.internal.DefaultTransactionCrudComponent
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionCrud

interface TransactionCrudComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            destination: DestinationTransactionCrud,
        ): TransactionCrudComponent {
            if (destination.transactionId == null && !destination.isIncome && !destination.isExpense) {
                error("Invalid destination: $destination")
            }
            return DefaultTransactionCrudComponent(
                componentContext = componentContext,
                transactionId = destination.transactionId,
                transactionType = if (destination.isIncome) TransactionType.Income else TransactionType.Expense,
            )
        }
    }
}
