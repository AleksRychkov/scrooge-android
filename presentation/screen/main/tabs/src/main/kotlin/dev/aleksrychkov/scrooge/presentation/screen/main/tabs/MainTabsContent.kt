package dev.aleksrychkov.scrooge.presentation.screen.main.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import dev.aleksrychkov.scrooge.presentation.screen.main.tabs.internal.MainTabsComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.ReportAnnualTotalContent
import dev.aleksrychkov.scrooge.presentation.screen.settings.SettingsContent
import dev.aleksrychkov.scrooge.presentation.screen.hub.HubContent
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val ROTATE_X = 0
private const val ROTATE_Y = 1
private const val ROTATE_Z = 2
private const val ANIM_DURATION = 1000
private const val ANIM_CAMERA_DISTANCE = 12f
private const val ANIM_FULL_TARGET_ANGLE = 360f

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
    ) {
        when (val child = it.instance) {
            is MainTabsComponentInternal.Child.Transactions -> HubContent(
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
                axis = ROTATE_Y,
                onClick = onHomeClicked
            )
            BottomBarItem(
                isSelected = activeComponent is MainTabsComponentInternal.Child.Report,
                icon = Icons.AutoMirrored.Filled.List,
                title = stringResource(Resources.string.reports),
                axis = ROTATE_X,
                onClick = onReportsClicked,
            )
            BottomBarItem(
                isSelected = activeComponent is MainTabsComponentInternal.Child.Settings,
                icon = Icons.Filled.Settings,
                title = stringResource(Resources.string.settings),
                axis = ROTATE_Z,
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
    axis: Int,
    onClick: () -> Unit
) {
    var wasSelected by remember { mutableStateOf(isSelected) }
    val shouldAnimate = !wasSelected && isSelected
    LaunchedEffect(isSelected) {
        wasSelected = isSelected
    }

    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(ANIM_DURATION)
    )

    val rotation by animateFloatAsState(
        targetValue = if (isSelected) ANIM_FULL_TARGET_ANGLE else 0f,
        animationSpec = if (shouldAnimate) tween(ANIM_DURATION) else snap()
    )

    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors().copy(
            selectedIndicatorColor = Color.Transparent,
            selectedIconColor = animatedColor,
            unselectedIconColor = animatedColor
        ),
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.graphicsLayer {
                    when (axis) {
                        ROTATE_X -> rotationX = rotation
                        ROTATE_Y -> rotationY = rotation
                        ROTATE_Z -> rotationZ = rotation
                    }
                    cameraDistance = ANIM_CAMERA_DISTANCE * density
                },
                tint = animatedColor
            )
        }
    )
}
