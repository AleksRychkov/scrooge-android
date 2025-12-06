package dev.aleksrychkov.scrooge.component.report.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.ReportComponent

internal interface ReportComponentInternal : ReportComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Annual : Child()
    }
}
