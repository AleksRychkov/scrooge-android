package dev.aleksrychkov.scrooge.component.main.tabs.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.main.tabs.MainTabsComponent
import dev.aleksrychkov.scrooge.component.report.root.ReportComponent
import dev.aleksrychkov.scrooge.component.settings.SettingsComponent
import dev.aleksrychkov.scrooge.component.transaction.root.TransactionsComponent

internal interface MainTabsComponentInternal : MainTabsComponent {
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
