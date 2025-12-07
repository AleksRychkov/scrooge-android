package dev.aleksrychkov.scrooge.component.mainTabs.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnDestroy
import dev.aleksrychkov.scrooge.component.report.root.ReportComponent
import dev.aleksrychkov.scrooge.component.settings.SettingsComponent
import dev.aleksrychkov.scrooge.component.transaction.root.TransactionsComponent
import kotlinx.serialization.Serializable

internal class DefaultMainTabsComponent(
    componentContext: ComponentContext,
) : MainTabsComponentInternal, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()
    private val backCallback = BackCallback {
        onTransactionsClicked()
    }

    init {
        backHandler.register(backCallback)
        doOnDestroy { backHandler.unregister(backCallback) }
        backCallback.isEnabled = false
    }

    override val stack: Value<ChildStack<*, MainTabsComponentInternal.Child>> =
        childStack(
            source = nav,
            serializer = Config.serializer(),
            initialConfiguration = Config.Transactions,
            handleBackButton = false,
            key = "DefaultMainTabsComponentStack",
            childFactory = ::child,
        )

    override fun onTransactionsClicked() {
        backCallback.isEnabled = false
        nav.replaceAll(Config.Transactions)
    }

    override fun onSettingsClicked() {
        backCallback.isEnabled = true
        if (stack.items.size == 1) {
            nav.bringToFront(Config.Settings)
        } else {
            nav.replaceCurrent(Config.Settings)
        }
    }

    override fun onReportsClicked() {
        backCallback.isEnabled = true
        if (stack.items.size == 1) {
            nav.bringToFront(Config.Report)
        } else {
            nav.replaceCurrent(Config.Report)
        }
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): MainTabsComponentInternal.Child =
        when (config) {
            Config.Transactions ->
                MainTabsComponentInternal.Child.Transactions(TransactionsComponent(componentContext = componentContext))

            Config.Report ->
                MainTabsComponentInternal.Child.Report(ReportComponent(componentContext = componentContext))

            Config.Settings ->
                MainTabsComponentInternal.Child.Settings(SettingsComponent(componentContext = componentContext))
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Transactions : Config

        @Serializable
        data object Report : Config

        @Serializable
        data object Settings : Config
    }
}
