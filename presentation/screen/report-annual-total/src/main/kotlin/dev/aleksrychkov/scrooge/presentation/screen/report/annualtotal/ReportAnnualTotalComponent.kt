package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.DefaultReportAnnualTotalComponent

interface ReportAnnualTotalComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): ReportAnnualTotalComponent {
            return DefaultReportAnnualTotalComponent(componentContext = componentContext)
        }
    }
}
