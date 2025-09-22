package dev.aleksrychkov.scrooge.component.report.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultReportComponent(
    private val componentContext: ComponentContext
) : ReportComponentInternal, ComponentContext by componentContext
