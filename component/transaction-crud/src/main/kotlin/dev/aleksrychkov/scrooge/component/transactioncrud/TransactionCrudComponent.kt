package dev.aleksrychkov.scrooge.component.transactioncrud

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.common.entity.TransactionType
import dev.aleksrychkov.scrooge.component.transactioncrud.internal.DefaultTransactionCrudComponent
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionCrud

interface TransactionCrudComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            destination: DestinationTransactionCrud,
        ): TransactionCrudComponent {
            check(destination.transactionId == null && !destination.isIncome && !destination.isExpense) {
                destination.transactionId == null && !destination.isIncome && !destination.isExpense
            }
            return DefaultTransactionCrudComponent(
                componentContext = componentContext,
                transactionId = destination.transactionId,
                transactionType = if (destination.isIncome) TransactionType.Income else TransactionType.Expense,
            )
        }
    }
}
