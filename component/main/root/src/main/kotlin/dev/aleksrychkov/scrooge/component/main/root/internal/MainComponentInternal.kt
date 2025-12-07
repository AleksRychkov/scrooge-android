package dev.aleksrychkov.scrooge.component.main.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.main.root.MainComponent
import dev.aleksrychkov.scrooge.component.main.tabs.MainTabsComponent
import dev.aleksrychkov.scrooge.component.transaction.form.TransactionFormComponent

internal interface MainComponentInternal : MainComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class MainTabs(val component: MainTabsComponent) : Child()
        class TransactionForm(val component: TransactionFormComponent) : Child()
    }
}
