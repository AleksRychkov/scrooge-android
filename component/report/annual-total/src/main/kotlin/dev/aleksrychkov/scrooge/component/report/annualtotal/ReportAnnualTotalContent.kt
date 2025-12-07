package dev.aleksrychkov.scrooge.component.report.annualtotal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.ReportAnnualTotalComponentInternal
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalContent
import dev.aleksrychkov.scrooge.core.designsystem.composables.noRippleClickable
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
        periodTotalComponent = component.periodTotalComponent,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: ReportAnnualTotalState,
    periodTotalComponent: PeriodTotalComponent,
) {
    var periodTotalHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val extraPaddingPx = remember { with(density) { Normal2X.roundToPx() } }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = with(density) { periodTotalHeight.toDp() })
        ) {}

        PeriodTotalContent(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    periodTotalHeight = size.height + extraPaddingPx
                },
            component = periodTotalComponent,
        )

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
