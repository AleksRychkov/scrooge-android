package dev.aleksrychkov.scrooge.presentation.screen.main.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.MainComponentInternal.Child.Limits
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.MainComponentInternal.Child.MainTabs
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.MainComponentInternal.Child.ReportCategoryTotal
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.MainComponentInternal.Child.TransactionForm
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.MainComponentInternal.Child.Transactions
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.navigation.MainNavigationConfig
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.navigation.MainRouter
import dev.aleksrychkov.scrooge.presentation.screen.main.tabs.MainTabsComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.ReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.presentation.screen.transactions.TransactionsComponent

internal class DefaultMainComponent(
    componentContext: ComponentContext,
) : MainComponentInternal, ComponentContext by componentContext {

    private val nav = StackNavigation<MainNavigationConfig>()
    private val router = MainRouter(navigation = nav)

    override val stack: Value<ChildStack<*, MainComponentInternal.Child>> =
        childStack(
            source = nav,
            serializer = MainNavigationConfig.serializer(),
            initialConfiguration = MainNavigationConfig.MainTabs,
            handleBackButton = true,
            key = "DefaultMainComponentStack",
            childFactory = ::child,
        )

    private fun child(
        config: MainNavigationConfig,
        componentContext: ComponentContext
    ): MainComponentInternal.Child {
        val routerComponentContext = RouterComponentContext(componentContext, router)
        return when (config) {
            MainNavigationConfig.MainTabs ->
                MainTabs(MainTabsComponent(componentContext = routerComponentContext))

            is MainNavigationConfig.TransactionForm -> TransactionForm(
                TransactionFormComponent(
                    componentContext = routerComponentContext,
                    destination = config.destination,
                )
            )

            is MainNavigationConfig.ReportCategoryTotal -> ReportCategoryTotal(
                ReportCategoryTotalComponent(
                    componentContext = routerComponentContext,
                    filter = config.destination.filter,
                )
            )

            is MainNavigationConfig.Transactions -> Transactions(
                TransactionsComponent(
                    componentContext = routerComponentContext,
                    filter = config.destination.filter,
                )
            )

            is MainNavigationConfig.Limits -> Limits(
                LimitsComponent(componentContext = routerComponentContext)
            )
        }
    }
}
