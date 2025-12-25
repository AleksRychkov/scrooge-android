package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal

import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.ReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.ByCategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.period.PeriodComponent

internal interface ReportCategoryTotalComponentInternal : ReportCategoryTotalComponent {
    val periodComponent: PeriodComponent
    val byCategoryComponent: ByCategoryComponent

    fun openPeriodModal()
    fun closePeriodModal()
}
