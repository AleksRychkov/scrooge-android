package dev.aleksrychkov.scrooge.presentation.component.periodtotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.DefaultPeriodTotalComponent

interface PeriodTotalComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): PeriodTotalComponent {
            return DefaultPeriodTotalComponent(componentContext = componentContext)
        }
    }

    fun setFilters(filterEntity: FilterEntity)
}
