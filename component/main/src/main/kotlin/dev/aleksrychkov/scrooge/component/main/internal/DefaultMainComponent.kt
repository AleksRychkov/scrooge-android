package dev.aleksrychkov.scrooge.component.main.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.main.internal.MainComponentInternal.Child.MainTabs
import dev.aleksrychkov.scrooge.component.main.internal.MainComponentInternal.Child.TransactionForm
import dev.aleksrychkov.scrooge.component.main.internal.navigation.MainNavigationConfig
import dev.aleksrychkov.scrooge.component.main.internal.navigation.MainRouter
import dev.aleksrychkov.scrooge.component.mainTabs.MainTabsComponent
import dev.aleksrychkov.scrooge.component.transaction.form.TransactionFormComponent
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext

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
        }
    }
}
