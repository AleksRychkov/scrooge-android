package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsFilterAction
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalContent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.ReportAnnualTotalComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.TotalMonthlyComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.TotalMonthlyContent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.modal.FiltersModal
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
            openCategoryReport = component::openCategoryTotal,
        )
    }
    FiltersModal(
        component = component,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportAppBar(
    component: ReportAnnualTotalComponentInternal,
    contentListState: LazyListState,
    elevation: Dp,
) {
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
    val state by component.state.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.reports))
            },
            actions = {
                val months = stringArrayResource(Resources.array.month_names)
                val shortMonths = stringArrayResource(Resources.array.short_month_names)
                val name = state.filter.readableName(months = months, shortMonths = shortMonths)
                DsFilterAction(
                    name = name,
                    showTagIcon = state.filter.tags.isNotEmpty(),
                    openFiltersModal = component::openFiltersModal,
                )
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
    openCategoryReport: (PeriodDatestampEntity) -> Unit,
) {
    Content(
        modifier = modifier,
        contentListState = contentListState,
        periodContentElevation = periodContentElevation,
        periodTotalComponent = component.periodTotalComponent,
        totalMonthlyComponent = component.totalMonthlyComponent,
        openCategoryReport = openCategoryReport,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    contentListState: LazyListState,
    periodContentElevation: Dp,
    periodTotalComponent: PeriodTotalComponent,
    totalMonthlyComponent: TotalMonthlyComponent,
    openCategoryReport: (PeriodDatestampEntity) -> Unit,
) {
    Box(modifier = modifier) {
        TotalMonthlyContent(
            modifier = Modifier.fillMaxWidth(),
            listState = contentListState,
            headerItem = {
                PeriodTotalContent(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = periodContentElevation,
                    component = periodTotalComponent,
                )
            },
            paddingBottom = Large2X,
            component = totalMonthlyComponent,
            openCategoryReport = openCategoryReport,
        )
    }
}
