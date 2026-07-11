package dev.aleksrychkov.scrooge.presentation.component.balancelinechart

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.DefaultBalanceTotalChartComponent

interface BalanceTotalChartComponent {
    fun setFilters(filter: FilterEntity)

    companion object {
        operator fun invoke(componentContext: ComponentContext): BalanceTotalChartComponent =
            DefaultBalanceTotalChartComponent(componentContext)
    }
}
