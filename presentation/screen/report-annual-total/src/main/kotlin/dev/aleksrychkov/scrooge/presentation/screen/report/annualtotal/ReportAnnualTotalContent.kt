package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsCardV2
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsFilterAction
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalContent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.ReportAnnualTotalComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.TotalMonthlyComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.TotalMonthlyContent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.modal.FiltersModal
import kotlinx.coroutines.launch
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
    Scaffold(
        modifier = modifier,
        topBar = {
            ReportAppBar(
                component = component,
                contentListState = contentListState,
            )
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
            contentListState = contentListState,
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
) {
    val headerElevation by remember {
        derivedStateOf {
            if (contentListState.firstVisibleItemScrollOffset > 0) {
                AppBarShadow
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
                Text(text = stringResource(Resources.string.totals))
            },
            actions = {
                val months = stringArrayResource(Resources.array.month_names)
                val shortMonths = stringArrayResource(Resources.array.short_month_names)
                val name = state.filter.readableName(months = months, shortMonths = shortMonths)
                val showFilterIcon = with(state.filter) {
                    tags.isNotEmpty() || category != null || transactionType != null
                }
                DsFilterAction(
                    name = name,
                    showFilterIcon = showFilterIcon,
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
    openCategoryReport: (PeriodDatestampEntity) -> Unit,
) {
    Content(
        modifier = modifier,
        contentListState = contentListState,
        periodTotalComponent = component.periodTotalComponent,
        totalMonthlyComponent = component.totalMonthlyComponent,
        openCategoryReport = openCategoryReport,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    contentListState: LazyListState,
    periodTotalComponent: PeriodTotalComponent,
    totalMonthlyComponent: TotalMonthlyComponent,
    openCategoryReport: (PeriodDatestampEntity) -> Unit,
) {
    val isScrillUpVisible by remember {
        derivedStateOf { contentListState.firstVisibleItemIndex != 0 }
    }
    Box(modifier = modifier) {
        TotalMonthlyContent(
            modifier = Modifier.fillMaxWidth(),
            listState = contentListState,
            headerItem = {
                DsCardV2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Large),
                ) {
                    PeriodTotalContent(
                        modifier = Modifier.fillMaxWidth(),
                        component = periodTotalComponent,
                    )
                }
            },
            paddingBottom = Large2X,
            component = totalMonthlyComponent,
            openCategoryReport = openCategoryReport,
        )

        AnimatedVisibility(
            visible = isScrillUpVisible,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            ScrollUpThumb(
                modifier = Modifier.fillMaxWidth(),
                contentListState = contentListState,
            )
        }
    }
}

@Composable
private fun ScrollUpThumb(
    modifier: Modifier,
    contentListState: LazyListState,
) {
    Box(
        modifier = modifier
            .padding(bottom = Large2X)
            .padding(end = Large),
        contentAlignment = Alignment.CenterEnd
    ) {
        val scope = rememberCoroutineScope()
        FloatingActionButton(
            onClick = {
                scope.launch {
                    contentListState.scrollToItem(0)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowUpward,
                contentDescription = null,
            )
        }
    }
}
