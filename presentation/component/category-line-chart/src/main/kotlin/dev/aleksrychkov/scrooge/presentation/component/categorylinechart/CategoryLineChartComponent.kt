package dev.aleksrychkov.scrooge.presentation.component.categorylinechart

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.DefaultCategoryLineChartComponent

interface CategoryLineChartComponent {
    fun setFilters(filter: FilterEntity)

    companion object {
        operator fun invoke(componentContext: ComponentContext): CategoryLineChartComponent =
            DefaultCategoryLineChartComponent(componentContext)
    }
}
