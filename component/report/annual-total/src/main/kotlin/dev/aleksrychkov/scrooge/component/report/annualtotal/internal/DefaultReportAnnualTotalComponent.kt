package dev.aleksrychkov.scrooge.component.report.annualtotal.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultReportAnnualTotalComponent(
    componentContext: ComponentContext
) : ReportAnnualTotalComponentInternal, ComponentContext by componentContext
