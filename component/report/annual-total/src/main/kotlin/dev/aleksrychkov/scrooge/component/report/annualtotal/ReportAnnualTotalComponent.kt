package dev.aleksrychkov.scrooge.component.report.annualtotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.DefaultReportAnnualTotalComponent

interface ReportAnnualTotalComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): ReportAnnualTotalComponent {
            return DefaultReportAnnualTotalComponent(componentContext = componentContext)
        }
    }
}
