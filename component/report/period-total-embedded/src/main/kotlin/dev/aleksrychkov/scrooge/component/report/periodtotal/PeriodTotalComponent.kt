package dev.aleksrychkov.scrooge.component.report.periodtotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.DefaultPeriodTotalComponent
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

interface PeriodTotalComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): PeriodTotalComponent {
            return DefaultPeriodTotalComponent(componentContext = componentContext)
        }
    }

    fun setPeriod(period: PeriodTimestampEntity)
}
