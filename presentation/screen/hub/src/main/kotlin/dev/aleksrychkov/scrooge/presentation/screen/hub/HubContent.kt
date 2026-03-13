package dev.aleksrychkov.scrooge.presentation.screen.hub

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsCardV2
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsFilterAction
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.presentation.component.limits.LimitsContent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalContent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListContent
import dev.aleksrychkov.scrooge.presentation.screen.hub.internal.HubComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.hub.internal.modal.FiltersModal
import dev.aleksrychkov.scrooge.presentation.screen.hub.internal.modal.FormModal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun HubContent(
    modifier: Modifier,
    component: HubComponent
) {
    HubContent(
        modifier = modifier,
        component = component as HubComponentInternal,
    )
}

@Composable
private fun HubContent(
    modifier: Modifier,
    component: HubComponentInternal
) {
    val contentListState = rememberLazyListState()
    val elevation = AppBarShadow
    Scaffold(
        modifier = modifier,
        topBar = {
            TransactionsAppBar(
                component = component,
                contentListState = contentListState,
                elevation = elevation
            )
        },
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentListState = contentListState,
            component = component,
        )
    }
    FiltersModal(component = component)
    FormModal(component = component)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsAppBar(
    component: HubComponentInternal,
    contentListState: LazyListState,
    elevation: Dp,
) {
    val headerElevation by remember {
        derivedStateOf {
            if (contentListState.firstVisibleItemScrollOffset > 0) {
                elevation
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()
    val state by component.state.collectAsStateWithLifecycle()

    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.scrooge))
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
    contentListState: LazyListState,
    component: HubComponentInternal
) {
    val isAddIncomeExpenseVisible by remember {
        derivedStateOf { contentListState.firstVisibleItemIndex == 0 }
    }

    Box(modifier = modifier) {
        TransactionsListContent(
            modifier = Modifier.fillMaxWidth(),
            listState = contentListState,
            headers = persistentListOf(
                {
                    DsCardV2(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Large),
                    ) {
                        PeriodTotalContent(
                            modifier = Modifier.fillMaxWidth(),
                            component = component.periodTotalComponent,
                        )
                    }
                },
                {
                    DsCardV2(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Large),
                    ) {
                        LimitsContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Normal, vertical = Large),
                            component = component.limitsComponent,
                        )
                    }
                }
            ),
            paddingBottom = 124.dp,
            component = component.transactionsListComponent,
        )

        AnimatedVisibility(
            visible = isAddIncomeExpenseVisible,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            AddIncomeExpense(
                modifier = Modifier.fillMaxWidth(),
                component = component,
            )
        }

        AnimatedVisibility(
            visible = !isAddIncomeExpenseVisible,
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

@Suppress("MagicNumber")
@Composable
private fun AddIncomeExpense(
    modifier: Modifier,
    component: HubComponentInternal,
) {
    Row(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background,
                    ),
                    endY = 100f,
                )
            )
            .padding(bottom = Large, top = Large2X)
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f, fill = true),
            contentAlignment = Alignment.Center,
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Large, end = Medium),
                onClick = { component.openIncomeModal() },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.AddCircleOutline,
                        contentDescription = stringResource(Resources.string.add_income),
                        tint = IncomeColor,
                    )
                },
                text = { Text(text = stringResource(Resources.string.add_income)) },
            )
        }
        Box(
            modifier = Modifier.weight(weight = 1f, fill = true),
            contentAlignment = Alignment.Center,
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = Large, start = Medium),
                onClick = { component.addExpense() },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.RemoveCircleOutline,
                        contentDescription = stringResource(Resources.string.add_expense),
                        tint = ExpenseColor,
                    )
                },
                text = { Text(text = stringResource(Resources.string.add_expense)) },
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
            .padding(bottom = Large)
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
