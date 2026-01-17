package dev.aleksrychkov.scrooge.presentation.screen.main.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.presentation.screen.main.root.MainComponent
import dev.aleksrychkov.scrooge.presentation.screen.main.tabs.MainTabsComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.ReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactions.TransactionsComponent

internal interface MainComponentInternal : MainComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class MainTabs(val component: MainTabsComponent) : Child()
        class TransactionForm(val component: TransactionFormComponent) : Child()
        class ReportCategoryTotal(val component: ReportCategoryTotalComponent) : Child()
        class Transactions(val component: TransactionsComponent) : Child()
    }
}
