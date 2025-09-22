package dev.aleksrychkov.scrooge.component.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.aleksrychkov.scrooge.component.main.internal.MainComponentInternal
import dev.aleksrychkov.scrooge.component.report.ReportContent
import dev.aleksrychkov.scrooge.component.settings.SettingsContent
import dev.aleksrychkov.scrooge.component.transactions.TransactionsContent

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
        modifier = modifier,
    ) {
        Tabs(
            modifier = Modifier
                .weight(1F)
                .consumeWindowInsets(WindowInsets.navigationBars),
            component = component,
        )
        BottomBar(
            modifier = Modifier.fillMaxWidth(),
            component = component,
        )
    }
}

@Composable
private fun Tabs(
    modifier: Modifier = Modifier,
    component: MainComponentInternal,
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is MainComponentInternal.Child.Transactions -> TransactionsContent(
                modifier = Modifier.fillMaxSize(),
                component = child.component,
            )

            is MainComponentInternal.Child.Report -> ReportContent(
                modifier = Modifier.fillMaxSize(),
                component = child.component,
            )

            is MainComponentInternal.Child.Settings -> SettingsContent(
                modifier = Modifier.fillMaxSize(),
                component = child.component,
            )
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    component: MainComponentInternal,
) {
    val stack by component.stack.subscribeAsState()

    BottomBar(
        modifier = modifier,
        stack = stack,
        onHomeClicked = component::onTransactionsClicked,
        onReportsClicked = component::onReportsClicked,
        onSettingsClicked = component::onSettingsClicked,
    )
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    stack: ChildStack<*, MainComponentInternal.Child>,
    onHomeClicked: () -> Unit,
    onReportsClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    val activeComponent = stack.active.instance

    Column(
        modifier = modifier
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
        ) {
            BottomBarItem(
                isSelected = activeComponent is MainComponentInternal.Child.Transactions,
                icon = Icons.Filled.Home,
                title = stringResource(R.string.component_transactions_title),
                onClick = onHomeClicked
            )
            BottomBarItem(
                isSelected = activeComponent is MainComponentInternal.Child.Report,
                icon = Icons.AutoMirrored.Filled.List,
                title = stringResource(R.string.component_report_title),
                onClick = onReportsClicked,
            )
            BottomBarItem(
                isSelected = activeComponent is MainComponentInternal.Child.Settings,
                icon = Icons.Filled.Settings,
                title = stringResource(R.string.component_settings_title),
                onClick = onSettingsClicked,
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    isSelected: Boolean,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val color = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text = title) },
        icon = {
            Icon(
                icon,
                tint = color,
                contentDescription = title,
            )
        }
    )
}
