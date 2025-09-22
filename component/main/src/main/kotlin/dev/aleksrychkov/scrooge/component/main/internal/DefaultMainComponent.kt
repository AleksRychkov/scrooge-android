package dev.aleksrychkov.scrooge.component.main.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.mainTabs.MainTabsComponent
import kotlinx.serialization.Serializable

internal class DefaultMainComponent(
    componentContext: ComponentContext,
) : MainComponentInternal, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MainComponentInternal.Child>> =
        childStack(
            source = nav,
            serializer = Config.serializer(),
            initialConfiguration = Config.MainTabs,
            handleBackButton = false,
            key = "DefaultMainComponentStack",
            childFactory = ::child,
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): MainComponentInternal.Child =
        when (config) {
            Config.MainTabs ->
                MainComponentInternal.Child.MainTabs(MainTabsComponent(componentContext = componentContext))
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object MainTabs : Config
    }
}
