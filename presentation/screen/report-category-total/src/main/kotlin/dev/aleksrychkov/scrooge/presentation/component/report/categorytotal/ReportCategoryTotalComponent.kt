package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.DefaultReportCategoryTotalComponent

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
