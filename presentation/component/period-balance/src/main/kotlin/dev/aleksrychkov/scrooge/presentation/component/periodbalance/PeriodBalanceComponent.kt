package dev.aleksrychkov.scrooge.presentation.component.periodbalance

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.periodbalance.internal.DefaultPeriodBalanceComponent

interface PeriodBalanceComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): PeriodBalanceComponent {
            return DefaultPeriodBalanceComponent(
                componentContext = componentContext,
            )
        }
    }

    fun setFilters(filter: FilterEntity)
}
