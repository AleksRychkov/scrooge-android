package dev.aleksrychkov.scrooge.component.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactions.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.BalanceComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.BalanceContent
import dev.aleksrychkov.scrooge.component.transactions.internal.composables.AddTransactionFab
import dev.aleksrychkov.scrooge.component.transactions.internal.modal.PeriodModal
import dev.aleksrychkov.scrooge.core.designsystem.composables.AppButton
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
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
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
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
            AppButton(
                modifier = Modifier.padding(end = Large),
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
    Box(
        modifier = modifier
    ) {
        ColumnContent(
            modifier = Modifier.fillMaxSize(),
            balanceComponent = component.balanceComponent,
        )
        AddTransactionFab(
            onIncomeClicked = component::addIncome,
            onExpenseClicked = component::addExpense,
        )
    }
}

@Composable
private fun ColumnContent(
    modifier: Modifier,
    balanceComponent: BalanceComponent,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            BalanceContent(
                modifier = Modifier.fillMaxWidth(),
                component = balanceComponent,
            )
        }
    }
}
