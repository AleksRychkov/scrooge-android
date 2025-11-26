package dev.aleksrychkov.scrooge.component.transactionslist

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactionslist.internal.DefaultTransactionsListComponent
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionsList

interface TransactionsListComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            destination: DestinationTransactionsList,
        ): TransactionsListComponent {
            return DefaultTransactionsListComponent(
                componentContext = componentContext,
                period = destination.periodFrom to destination.periodTo,
            )
        }
    }
}
