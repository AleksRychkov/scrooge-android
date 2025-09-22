package dev.aleksrychkov.scrooge.component.report

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.internal.DefaultReportComponent

interface ReportComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): ReportComponent = DefaultReportComponent(
            componentContext = componentContext,
        )
    }
}
