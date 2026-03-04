package dev.aleksrychkov.scrooge.presentation.screen.main.root.internal

import android.annotation.SuppressLint
import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
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
    deeplink: Uri?,
) : MainComponentInternal, ComponentContext by componentContext {

    private companion object {
        private const val DEEPLINK_ADD_INCOME_PATH = "/add_income"
        private const val DEEPLINK_ADD_EXPENSE_PATH = "/add_expense"
    }

    private val nav = StackNavigation<MainNavigationConfig>()
    private val router = MainRouter(navigation = nav)

    override val stack: Value<ChildStack<*, MainComponentInternal.Child>> =
        childStack(
            source = nav,
            serializer = MainNavigationConfig.serializer(),
            initialStack = { initialStack(deeplink) },
            handleBackButton = true,
            key = "DefaultMainComponentStack",
            childFactory = ::child,
        )

    @SuppressLint("UseKtx")
    override fun handleDeeplink(deeplink: String) {
        if (stack.value.active.configuration is MainNavigationConfig.TransactionForm) return
        Uri.parse(deeplink).toNavConfig()?.let { nav.pushNew(it) }
    }

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

    private fun initialStack(deeplink: Uri?): List<MainNavigationConfig> {
        val res = mutableListOf<MainNavigationConfig>()
        res.add(MainNavigationConfig.MainTabs)
        deeplink?.toNavConfig()?.let(res::add)
        return res.toList()
    }

    private fun Uri?.toNavConfig(): MainNavigationConfig? {
        return when (this?.path) {
            DEEPLINK_ADD_INCOME_PATH -> MainNavigationConfig.TransactionForm(
                DestinationTransactionForm.addIncome() as DestinationTransactionForm
            )

            DEEPLINK_ADD_EXPENSE_PATH -> MainNavigationConfig.TransactionForm(
                DestinationTransactionForm.addExpense() as DestinationTransactionForm
            )

            else -> null
        }
    }
}
