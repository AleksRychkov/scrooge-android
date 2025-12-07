package dev.aleksrychkov.scrooge.component.report.annualtotal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.ReportAnnualTotalComponentInternal
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import dev.aleksrychkov.scrooge.core.designsystem.composables.noRippleClickable
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun ReportAnnualTotalContent(
    modifier: Modifier,
    component: ReportAnnualTotalComponent,
) {
    ReportAnnualTotalContent(
        modifier = modifier,
        component = component as ReportAnnualTotalComponentInternal,
    )
}

@Composable
private fun ReportAnnualTotalContent(
    modifier: Modifier,
    component: ReportAnnualTotalComponentInternal,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ReportAppBar(component = component)
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
private fun ReportAppBar(
    component: ReportAnnualTotalComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()
    TopAppBar(
        title = {
            Text(text = stringResource(Resources.string.reports))
        },
        actions = {
            TextButton(onClick = component::openPeriodModal) {
                Text(text = state.selectedYear.toString())
            }
        }
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    component: ReportAnnualTotalComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: ReportAnnualTotalState,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {}

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {},
                contentAlignment = Alignment.Center,
            ) {}
        }
    }
}
