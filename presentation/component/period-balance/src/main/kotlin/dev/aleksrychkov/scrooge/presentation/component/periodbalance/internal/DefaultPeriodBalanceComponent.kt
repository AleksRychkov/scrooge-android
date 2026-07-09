package dev.aleksrychkov.scrooge.presentation.component.periodbalance.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal class DefaultPeriodBalanceComponent(
    componentContext: ComponentContext,
) : PeriodBalanceComponentInternal, ComponentContext by componentContext {

    @Suppress("EmptyFunctionBlock")
    override fun setFilters(filter: FilterEntity) {}
}
