package dev.aleksrychkov.scrooge.component.report.annualtotal

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.ReportAnnualTotalComponentInternal
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalContent
import dev.aleksrychkov.scrooge.core.designsystem.composables.noRippleClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
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
    val contentListState = rememberLazyListState()
    val elevation = Medium
    Scaffold(
        modifier = modifier,
        topBar = {
            ReportAppBar(
                component = component,
                contentListState = contentListState,
                elevation = elevation,
            )
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
            contentListState = contentListState,
            periodContentElevation = elevation,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportAppBar(
    component: ReportAnnualTotalComponentInternal,
    contentListState: LazyListState,
    elevation: Dp,
) {
    val state by component.state.collectAsStateWithLifecycle()
    val headerElevation by remember {
        derivedStateOf {
            if (contentListState.firstVisibleItemIndex > 0) {
                elevation
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
                Text(text = stringResource(Resources.string.reports))
            },
            actions = {
                TextButton(onClick = component::openPeriodModal) {
                    Text(text = state.selectedYear.toString())
                }
            }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    component: ReportAnnualTotalComponentInternal,
    contentListState: LazyListState,
    periodContentElevation: Dp,
) {
    val state by component.state.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        state = state,
        contentListState = contentListState,
        periodContentElevation = periodContentElevation,
        periodTotalComponent = component.periodTotalComponent,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: ReportAnnualTotalState,
    contentListState: LazyListState,
    periodContentElevation: Dp,
    periodTotalComponent: PeriodTotalComponent,
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = contentListState,
            verticalArrangement = Arrangement.spacedBy(Normal2X),
        ) {
            item {
                PeriodTotalContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Normal),
                    component = periodTotalComponent,
                    elevation = periodContentElevation,
                )
            }
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

@Composable
private fun Dp.animateElevation(durationMillis: Int = 300): State<Dp> {
    return animateDpAsState(
        targetValue = this,
        animationSpec = tween(durationMillis = durationMillis),
        label = "toolbar shadow animation"
    )
}
