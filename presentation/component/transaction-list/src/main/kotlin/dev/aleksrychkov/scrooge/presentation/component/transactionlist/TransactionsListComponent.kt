package dev.aleksrychkov.scrooge.presentation.component.transactionlist

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.DefaultTransactionsListComponent

interface TransactionsListComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            period: PeriodTimestampEntity,
        ): TransactionsListComponent {
            return DefaultTransactionsListComponent(
                componentContext = componentContext,
                period = period,
            )
        }
    }

    fun setPeriod(period: PeriodTimestampEntity)
}
