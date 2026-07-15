package dev.aleksrychkov.scrooge.presentation.component.balancelinechart

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.DefaultIncomeExpenseChartComponent

interface IncomeExpenseChartComponent {
    fun setFilters(filter: FilterEntity)

    companion object {
        operator fun invoke(componentContext: ComponentContext): IncomeExpenseChartComponent =
            DefaultIncomeExpenseChartComponent(componentContext)
    }
}
