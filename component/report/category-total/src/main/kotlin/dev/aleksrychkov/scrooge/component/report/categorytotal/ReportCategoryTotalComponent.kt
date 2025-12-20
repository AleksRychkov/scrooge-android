package dev.aleksrychkov.scrooge.component.report.categorytotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.DefaultReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

interface ReportCategoryTotalComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            period: PeriodTimestampEntity,
        ): ReportCategoryTotalComponent {
            return DefaultReportCategoryTotalComponent(
                componentContext = componentContext,
                period = period,
            )
        }
    }
}
