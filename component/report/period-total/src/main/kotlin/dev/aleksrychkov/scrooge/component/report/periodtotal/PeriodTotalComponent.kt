package dev.aleksrychkov.scrooge.component.report.periodtotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.DefaultPeriodTotalComponent

interface PeriodTotalComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): PeriodTotalComponent {
            return DefaultPeriodTotalComponent(componentContext = componentContext)
        }
    }

    fun setPeriod(fromTimestamp: Long, toTimestamp: Long)
}
