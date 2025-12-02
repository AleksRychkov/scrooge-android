package dev.aleksrychkov.scrooge.component.transactionslist

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactionslist.internal.DefaultTransactionsListComponent

interface TransactionsListComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            period: Pair<Long, Long>,
        ): TransactionsListComponent {
            return DefaultTransactionsListComponent(
                componentContext = componentContext,
                period = period,
            )
        }
    }

    fun setPeriod(period: Pair<Long, Long>)
}
