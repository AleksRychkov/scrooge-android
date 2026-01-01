package dev.aleksrychkov.scrooge.presentation.screen.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsFilterAction
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalContent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListContent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.modal.FiltersModal
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponent
) {
    TransactionsContent(
        modifier = modifier,
        component = component as TransactionsComponentInternal,
    )
}

@Composable
private fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponentInternal
) {
    val contentListState = rememberLazyListState()
    val elevation = Medium
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
            periodContentElevation = elevation,
        )
    }
    FiltersModal(component = component)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsAppBar(
    component: TransactionsComponentInternal,
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
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.transactions))
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
    contentListState: LazyListState,
    periodContentElevation: Dp,
    component: TransactionsComponentInternal
) {
    val isAddIncomeExpenseVisible by remember {
        derivedStateOf { contentListState.firstVisibleItemIndex == 0 }
    }

    Box(modifier = modifier) {
        TransactionsListContent(
            modifier = Modifier.fillMaxWidth(),
            listState = contentListState,
            headerItem = {
                PeriodTotalContent(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = periodContentElevation,
                    component = component.periodTotalComponent,
                )
            },
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

@Composable
private fun AddIncomeExpense(
    modifier: Modifier,
    component: TransactionsComponentInternal,
) {
    Row(
        modifier = modifier.padding(bottom = Large2X)
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f, fill = true),
            contentAlignment = Alignment.Center,
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Large, end = Medium),
                onClick = { component.addIncome() },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(Resources.drawable.ic_trending_up_24px),
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
                        imageVector = ImageVector.vectorResource(Resources.drawable.ic_trending_down_24px),
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

@Composable
private fun Dp.animateElevation(durationMillis: Int = 300): State<Dp> {
    return animateDpAsState(
        targetValue = this,
        animationSpec = tween(durationMillis = durationMillis),
        label = "toolbar shadow animation"
    )
}
