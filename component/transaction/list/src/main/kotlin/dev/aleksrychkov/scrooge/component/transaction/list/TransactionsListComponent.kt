package dev.aleksrychkov.scrooge.component.transaction.list

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transaction.list.internal.DefaultTransactionsListComponent
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

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
