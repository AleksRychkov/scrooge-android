package dev.aleksrychkov.scrooge.component.transaction.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transaction.list.TransactionsListContent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.BalanceContent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.modal.PeriodModal
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
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

@Suppress("unused")
@Composable
private fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponentInternal
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TransactionsAppBar(
                component = component,
            )
        },
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
        )
    }
    PeriodModal(component = component)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsAppBar(
    component: TransactionsComponentInternal
) {
    val state by component.state.collectAsStateWithLifecycle()

    TopAppBar(
        title = {
            Text(text = stringResource(Resources.string.transactions))
        },
        actions = {
            TextButton(
                onClick = {
                    component.openPeriodModal(state.selectedPeriod)
                },
            ) {
                Text(text = state.selectedPeriodReadable)
            }
        }
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    component: TransactionsComponentInternal
) {
    var balanceHeight by remember { mutableIntStateOf(0) }
    var addIncomeExpenseHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val extraPaddingPx = remember { with(density) { Normal2X.roundToPx() } }

    Box(modifier = modifier) {
        TransactionsListContent(
            modifier = Modifier.fillMaxWidth(),
            paddingTop = balanceHeight,
            paddingBottom = addIncomeExpenseHeight,
            component = component.transactionsListComponent,
        )
        BalanceContent(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    balanceHeight = size.height + extraPaddingPx
                },
            component = component.balanceComponent,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            AddIncomeExpense(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size ->
                        addIncomeExpenseHeight = size.height + extraPaddingPx
                    },
                component = component,
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
