package dev.aleksrychkov.scrooge.component.report.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.component.report.categorytotal.ReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.component.report.root.ReportComponent
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

internal interface ReportComponentInternal : ReportComponent {
    val stack: Value<ChildStack<*, Child>>

    fun openCategoryReport(period: PeriodTimestampEntity)

    sealed class Child {
        class AnnualTotal(val component: ReportAnnualTotalComponent) : Child()
        class CategoryTotal(val component: ReportCategoryTotalComponent) : Child()
    }
}
