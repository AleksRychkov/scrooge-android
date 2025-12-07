package dev.aleksrychkov.scrooge.component.main.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.aleksrychkov.scrooge.component.main.root.internal.MainComponentInternal
import dev.aleksrychkov.scrooge.component.main.tabs.MainTabsContent
import dev.aleksrychkov.scrooge.component.transaction.form.TransactionFormContent

@Composable
fun MainContent(
    modifier: Modifier,
    component: MainComponent,
) {
    MainContent(
        modifier = modifier,
        component = component as MainComponentInternal,
    )
}

@Composable
private fun MainContent(
    modifier: Modifier,
    component: MainComponentInternal,
) {
    Column(
        modifier = modifier
    ) {
        Children(
            stack = component.stack,
            modifier = Modifier.fillMaxSize(),
            animation = stackAnimation(fade()),
        ) {
            when (val child = it.instance) {
                is MainComponentInternal.Child.MainTabs -> MainTabsContent(
                    modifier = Modifier.fillMaxSize(),
                    component = child.component,
                )

                is MainComponentInternal.Child.TransactionForm -> TransactionFormContent(
                    modifier = Modifier.fillMaxSize(),
                    component = child.component,
                )
            }
        }
    }
}
