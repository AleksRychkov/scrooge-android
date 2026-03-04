package dev.aleksrychkov.scrooge.presentation.screen.root.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentation.screen.main.root.MainContent
import dev.aleksrychkov.scrooge.presentation.screen.root.internal.component.IntermediateContent
import dev.aleksrychkov.scrooge.presentation.screen.transfer.RootTransferContent

@Composable
internal fun RootContent(
    modifier: Modifier = Modifier,
    componentContext: ComponentContext,
    readyCallback: () -> Unit,
) {
    val component = remember { DefaultRootComponent(componentContext = componentContext) }
    RootContent(
        modifier = modifier,
        component = component,
    )

    ReadyCallback(
        component = component,
        readyCallback = readyCallback,
    )
}

@Composable
private fun RootContent(
    modifier: Modifier = Modifier,
    component: DefaultRootComponent
) {
    Children(
        modifier = modifier.fillMaxSize(),
        stack = component.stack,
        animation = stackAnimation(fade()),
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Main -> MainContent(
                modifier = Modifier.fillMaxSize(),
                component = instance.component,
            )

            is RootComponent.Child.Intermediate -> IntermediateContent(
                modifier = Modifier.fillMaxSize(),
            )

            is RootComponent.Child.Transfer -> RootTransferContent(
                modifier = Modifier.fillMaxSize(),
                component = instance.component,
            )
        }
    }
}

@Composable
private fun ReadyCallback(
    component: DefaultRootComponent,
    readyCallback: () -> Unit,
) {
    val stack by component.stack.subscribeAsState()
    var readyCalled by remember { mutableStateOf(false) }
    LaunchedEffect(stack) {
        if (!readyCalled && stack.active.instance !is RootComponent.Child.Intermediate) {
            readyCalled = true
            readyCallback()
        }
    }
}
