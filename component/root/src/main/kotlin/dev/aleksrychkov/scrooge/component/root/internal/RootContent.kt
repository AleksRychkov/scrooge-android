package dev.aleksrychkov.scrooge.component.root.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.aleksrychkov.scrooge.component.main.MainContent

@Composable
internal fun RootContent(
    modifier: Modifier = Modifier,
    componentContext: ComponentContext,
) {
    val component = DefaultRootComponent(
        componentContext = componentContext,
    )
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
