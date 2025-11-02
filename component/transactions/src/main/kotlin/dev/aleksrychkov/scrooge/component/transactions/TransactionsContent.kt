package dev.aleksrychkov.scrooge.component.transactions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactions.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.component.transactions.internal.composables.AddTransactionFab
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsState
import dev.aleksrychkov.scrooge.core.designsystem.composables.AppButton
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
            TransactionsAppBar()
        },
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
private fun TransactionsAppBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(Resources.string.transactions))
        },
        actions = {
            AppButton(
                modifier = Modifier.padding(end = Large),
                onClick = {},
            ) {
                Text(text = "November 2025")
            }
        }
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    component: TransactionsComponentInternal
) {
    val state by component.state.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
    ) {
        ColumnContent(
            modifier = Modifier.fillMaxSize(),
            state = state,
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
    state: TransactionsState
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Balance(
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isLoading,
            )
        }
    }
}

@Composable
private fun Balance(
    modifier: Modifier,
    isLoading: Boolean,
) {
    Column(
        modifier = modifier
            .padding(Large)
            .background(
                shape = CardDefaults.shape,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
            .clip(CardDefaults.shape)
            .padding(Normal),
    ) {
        BalanceItem(
            icon = Resources.drawable.ic_trending_up_24px,
            title = Resources.string.income,
            isLoading = isLoading,
            value = "1123454$",
            color = IncomeColor,
        )
        Spacer(modifier = Modifier.height(Normal2X))

        BalanceItem(
            icon = Resources.drawable.ic_trending_down_24px,
            title = Resources.string.expense,
            isLoading = isLoading,
            value = "1123454$",
            color = ExpenseColor,
        )
        Spacer(modifier = Modifier.height(Normal2X))

        BalanceItem(
            icon = Resources.drawable.ic_balance_24px,
            title = Resources.string.total,
            isLoading = isLoading,
            value = "1123454$",
            color = Color.Unspecified,
        )
    }
}

@Composable
private fun BalanceItem(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    isLoading: Boolean,
    value: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = color,
        )
        Spacer(Modifier.size(Large))
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
        AnimatedVisibility(
            visible = isLoading
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp)
            )
        }
        AnimatedVisibility(
            visible = !isLoading
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = value,
                textAlign = TextAlign.End,
                color = color,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}
