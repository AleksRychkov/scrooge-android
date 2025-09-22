package dev.aleksrychkov.scrooge.component.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.main.MainComponent
import kotlinx.serialization.Serializable

internal class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Main,
            handleBackButton = true,
            key = "DefaultRootComponentStack",
            childFactory = ::child,
        )

    private fun child(
        configuration: Configuration,
        childComponentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is Configuration.Main -> RootComponent.Child.Main(MainComponent(childComponentContext))
        }

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object Main : Configuration
    }
}
