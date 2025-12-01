package dev.aleksrychkov.scrooge.component.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactions.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.BalanceComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.BalanceContent
import dev.aleksrychkov.scrooge.component.transactions.internal.modal.PeriodModal
import dev.aleksrychkov.scrooge.core.designsystem.composables.AppButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
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
    Column(
        modifier = modifier
    ) {
        ColumnContent(
            modifier = Modifier.weight(1f),
            balanceComponent = component.balanceComponent,
        )
        AddIncomeExpense(
            modifier = Modifier.fillMaxWidth(),
            component = component,
        )
    }
}

@Composable
private fun AddIncomeExpense(
    modifier: Modifier,
    component: TransactionsComponentInternal,
) {
    Row(
        modifier = modifier.padding(bottom = Normal2X)
    ) {
        Box(
            modifier = Modifier
                .weight(weight = 1f, fill = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .debounceClickable {
                        component.addIncome()
                    }
                    .padding(Normal2X),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .height(52.dp)
                        .width(52.dp)
                        .clip(CircleShape)
                        .background(IncomeColor)
                        .padding(Normal),
                    tint = Color.White,
                    imageVector = ImageVector.vectorResource(Resources.drawable.ic_trending_up_24px),
                    contentDescription = null,
                )
                Spacer(Modifier.size(Large))
                Text(stringResource(Resources.string.add_income))
            }
        }
        Box(modifier = Modifier.weight(weight = 1f, fill = true)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .debounceClickable {
                        component.addExpense()
                    }
                    .padding(Normal2X),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .height(52.dp)
                        .width(52.dp)
                        .clip(CircleShape)
                        .background(ExpenseColor)
                        .padding(Normal),
                    tint = Color.White,
                    imageVector = ImageVector.vectorResource(Resources.drawable.ic_trending_down_24px),
                    contentDescription = null,
                )
                Spacer(Modifier.size(Large))
                Text(stringResource(Resources.string.add_expense))
            }
        }
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
