package dev.aleksrychkov.scrooge.component.main.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.main.MainComponent
import dev.aleksrychkov.scrooge.component.report.ReportComponent
import dev.aleksrychkov.scrooge.component.settings.SettingsComponent
import dev.aleksrychkov.scrooge.component.transactions.TransactionsComponent

internal interface MainComponentInternal : MainComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onTransactionsClicked()
    fun onReportsClicked()
    fun onSettingsClicked()

    sealed class Child {
        class Transactions(val component: TransactionsComponent) : Child()
        class Report(val component: ReportComponent) : Child()
        class Settings(val component: SettingsComponent) : Child()
    }
}
