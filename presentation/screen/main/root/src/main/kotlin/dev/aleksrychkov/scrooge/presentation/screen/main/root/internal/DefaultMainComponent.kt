package dev.aleksrychkov.scrooge.presentation.screen.main.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.transaction.form.TransactionFormComponent
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.navigation.MainNavigationConfig
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.navigation.MainRouter
import dev.aleksrychkov.scrooge.presentation.screen.main.tabs.MainTabsComponent

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
                MainComponentInternal.Child.MainTabs(MainTabsComponent(componentContext = routerComponentContext))

            is MainNavigationConfig.TransactionForm -> MainComponentInternal.Child.TransactionForm(
                TransactionFormComponent(
                    componentContext = routerComponentContext,
                    destination = config.destination,
                )
            )
        }
    }
}
