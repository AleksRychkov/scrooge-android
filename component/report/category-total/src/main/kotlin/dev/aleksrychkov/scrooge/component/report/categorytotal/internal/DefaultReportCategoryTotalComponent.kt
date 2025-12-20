package dev.aleksrychkov.scrooge.component.report.categorytotal.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

@Suppress("UnusedPrivateProperty")
internal class DefaultReportCategoryTotalComponent(
    componentContext: ComponentContext,
    private val period: PeriodTimestampEntity,
) : ReportCategoryTotalComponentInternal, ComponentContext by componentContext
