package dev.aleksrychkov.scrooge.presentation.screen.limits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.LimitsComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
    Scaffold(
        modifier = modifier,
        topBar = {
            LimitsAppBar(component = component)
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LimitsAppBar(
    component: LimitsComponentInternal,
) {
    Surface(
        Modifier.fillMaxWidth(),
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

@Suppress("UnusedParameter")
@Composable
private fun Content(
    modifier: Modifier,
    component: LimitsComponentInternal,
) {
    Column(
        modifier = modifier
    ) {
        Text("LIMITS")
    }
}
