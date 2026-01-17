package dev.aleksrychkov.scrooge.presentation.screen.main.tabs.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.presentation.screen.hub.HubComponent
import dev.aleksrychkov.scrooge.presentation.screen.main.tabs.MainTabsComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.presentation.screen.settings.SettingsComponent

internal interface MainTabsComponentInternal : MainTabsComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onTransactionsClicked()
    fun onReportsClicked()
    fun onSettingsClicked()

    sealed class Child {
        class Transactions(val component: HubComponent) : Child()
        class Report(val component: ReportAnnualTotalComponent) : Child()
        class Settings(val component: SettingsComponent) : Child()
    }
}
