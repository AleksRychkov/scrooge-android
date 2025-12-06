package dev.aleksrychkov.scrooge.component.report.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.component.report.root.ReportComponent

internal interface ReportComponentInternal : ReportComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Annual(val component: ReportAnnualTotalComponent) : Child()
    }
}
