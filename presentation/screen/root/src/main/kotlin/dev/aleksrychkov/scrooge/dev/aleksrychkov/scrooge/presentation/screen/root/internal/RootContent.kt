package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.aleksrychkov.scrooge.presentation.screen.main.root.MainContent

@Composable
internal fun RootContent(
    modifier: Modifier = Modifier,
    componentContext: ComponentContext,
) {
    val component = remember { DefaultRootComponent(componentContext = componentContext) }
    RootContent(
        modifier = modifier,
        component = component,
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
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Main -> MainContent(
                modifier = Modifier.fillMaxSize(),
                component = instance.component,
            )
        }
    }
}
