package dev.aleksrychkov.scrooge.component.report.categorytotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.DefaultReportCategoryTotalComponent

interface ReportCategoryTotalComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
        ): ReportCategoryTotalComponent {
            return DefaultReportCategoryTotalComponent(
                componentContext = componentContext,
            )
        }
    }
}
