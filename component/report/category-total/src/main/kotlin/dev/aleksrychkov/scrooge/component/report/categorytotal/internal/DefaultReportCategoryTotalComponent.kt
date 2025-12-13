package dev.aleksrychkov.scrooge.component.report.categorytotal.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultReportCategoryTotalComponent(
    componentContext: ComponentContext,
) : ReportCategoryTotalComponentInternal, ComponentContext by componentContext
