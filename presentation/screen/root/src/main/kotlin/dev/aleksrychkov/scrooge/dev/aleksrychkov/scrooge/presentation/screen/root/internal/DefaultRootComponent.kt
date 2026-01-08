package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.RootComponent.Child.Intermediate
import dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.RootComponent.Child.Main
import dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.RootComponent.Child.Transfer
import dev.aleksrychkov.scrooge.feature.transfer.ObserveTransferStateUseCase
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.RootTransferComponent
import dev.aleksrychkov.scrooge.presentation.screen.main.root.MainComponent
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

internal class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    private val transferStateUseCase: ObserveTransferStateUseCase = get()

    init {
        transferStateUseCase
            .invoke()
            .distinctUntilChanged()
            .onEach {
                if (it.current is TransferStateEntity.State.None) {
                    navigation.replaceAll(Configuration.Main)
                } else {
                    navigation.replaceAll(Configuration.Transfer(state = it))
                }
            }
            .launchIn(retainedCoroutineScope())
    }

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Intermediate,
            handleBackButton = true,
            key = "DefaultRootComponentStack",
            childFactory = ::child,
        )

    private fun child(
        configuration: Configuration,
        childComponentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is Configuration.Main -> Main(MainComponent(childComponentContext))
            is Configuration.Intermediate -> Intermediate()
            is Configuration.Transfer -> Transfer(
                RootTransferComponent(
                    componentContext = childComponentContext,
                    state = configuration.state,
                )
            )
        }

    @Serializable
    private sealed interface Configuration {

        @Serializable
        data object Intermediate : Configuration

        @Serializable
        data object Main : Configuration

        @Serializable
        data class Transfer(val state: TransferStateEntity) : Configuration
    }
}
