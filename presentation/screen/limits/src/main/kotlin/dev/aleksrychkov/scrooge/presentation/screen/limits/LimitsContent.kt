package dev.aleksrychkov.scrooge.presentation.screen.limits

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.LimitsComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW = 10

@Composable
fun LimitsContent(
    modifier: Modifier,
    component: LimitsComponent,
) {
    LimitsContent(
        modifier = modifier,
        component = component as LimitsComponentInternal,
    )
}

@Composable
private fun LimitsContent(
    modifier: Modifier,
    component: LimitsComponentInternal,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier,
        topBar = {
            LimitsAppBar(
                scrollState = scrollState,
                component = component,
            )
        }
    ) { innerPadding ->
        ContentIme(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
            scrollState = scrollState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LimitsAppBar(
    scrollState: ScrollState,
    component: LimitsComponentInternal,
) {
    val headerElevation by remember {
        derivedStateOf {
            if (scrollState.value > MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW) {
                AppBarShadow
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()

    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.limits))
            },
            navigationIcon = {
                IconButton(onClick = component::onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Resources.string.back),
                    )
                }
            },
        )
    }
}

@Composable
private fun ContentIme(
    modifier: Modifier,
    component: LimitsComponentInternal,
    scrollState: ScrollState,
) {
    Scaffold(
        modifier = modifier,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
                .imePadding()
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                component = component,
                scrollState = scrollState,
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    component: LimitsComponentInternal,
    scrollState: ScrollState,
) {
    Content(
        modifier = modifier,
        scrollState = scrollState,
        onSaveClicked = component::onSaveClicked,
        onAddLimitClicked = component::onAddLimitClicked,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    scrollState: ScrollState,
    onAddLimitClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(Large)
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            onClick = onAddLimitClicked,
        ) {
            Text(stringResource(Resources.string.limits_add))
        }

        Spacer(modifier = Modifier.weight(1f, fill = true))

        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            onClick = onSaveClicked,
        ) {
            Text(stringResource(Resources.string.save))
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Content(
                modifier = Modifier.fillMaxSize(),
                scrollState = rememberScrollState(),
                onSaveClicked = {},
                onAddLimitClicked = {},
            )
        }
    }
}
