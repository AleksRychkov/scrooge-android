package dev.aleksrychkov.scrooge.presentation.screen.main.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.aleksrychkov.scrooge.presentation.screen.main.tabs.internal.MainTabsComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.ReportAnnualTotalContent
import dev.aleksrychkov.scrooge.presentation.screen.settings.SettingsContent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.TransactionsContent
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun MainTabsContent(
    modifier: Modifier,
    component: MainTabsComponent,
) {
    MainContent(
        modifier = modifier,
        component = component as MainTabsComponentInternal,
    )
}

@Composable
private fun MainContent(
    modifier: Modifier,
    component: MainTabsComponentInternal,
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
    component: MainTabsComponentInternal,
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is MainTabsComponentInternal.Child.Transactions -> TransactionsContent(
                modifier = Modifier.fillMaxSize(),
                component = child.component,
            )

            is MainTabsComponentInternal.Child.Report -> ReportAnnualTotalContent(
                modifier = Modifier.fillMaxSize(),
                component = child.component,
            )

            is MainTabsComponentInternal.Child.Settings -> SettingsContent(
                modifier = Modifier.fillMaxSize(),
                component = child.component,
            )
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    component: MainTabsComponentInternal,
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
    stack: ChildStack<*, MainTabsComponentInternal.Child>,
    onHomeClicked: () -> Unit,
    onReportsClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    val activeComponent = stack.active.instance

    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.1f),
        )
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp), // copied from NavigationBarHeight
        ) {
            BottomBarItem(
                isSelected = activeComponent is MainTabsComponentInternal.Child.Transactions,
                icon = Icons.Filled.Home,
                title = stringResource(Resources.string.transactions),
                onClick = onHomeClicked
            )
            BottomBarItem(
                isSelected = activeComponent is MainTabsComponentInternal.Child.Report,
                icon = Icons.AutoMirrored.Filled.List,
                title = stringResource(Resources.string.reports),
                onClick = onReportsClicked,
            )
            BottomBarItem(
                isSelected = activeComponent is MainTabsComponentInternal.Child.Settings,
                icon = Icons.Filled.Settings,
                title = stringResource(Resources.string.settings),
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
        MaterialTheme.colorScheme.onBackground
    }
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors().copy(
            selectedIndicatorColor = Color.Transparent,
            selectedIconColor = color
        ),
        icon = {
            Icon(
                icon,
                contentDescription = title,
            )
        }
    )
}
