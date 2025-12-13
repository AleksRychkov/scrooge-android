package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.totalMonthly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyState
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun TotalMonthlyContent(
    modifier: Modifier,
    listState: LazyListState? = null,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    headerItem: @Composable (() -> Unit)? = null,
    component: TotalMonthlyComponent,
    openCategoryReport: () -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    TotalMonthlyContent(
        modifier = modifier,
        listState = listState,
        headerItem = headerItem,
        paddingTop = paddingTop,
        paddingBottom = paddingBottom,
        state = state,
        openCategoryReport = openCategoryReport,
    )
}

@Composable
private fun TotalMonthlyContent(
    modifier: Modifier,
    state: TotalMonthlyState,
    listState: LazyListState? = null,
    headerItem: @Composable (() -> Unit)? = null,
    paddingTop: Dp,
    paddingBottom: Dp,
    openCategoryReport: () -> Unit,
) {
    val listState = listState ?: rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            top = paddingTop,
            bottom = paddingBottom,
        ),
        verticalArrangement = Arrangement.spacedBy(Normal2X),
    ) {
        if (headerItem != null) {
            item {
                headerItem()
            }
        }

        items(
            items = state.byMonth,
            key = { it.month }
        ) { byMonth ->
            TotalByMonth(
                modifier = Modifier
                    .fillMaxWidth()
                    .debounceClickable {
                        openCategoryReport()
                    },
                byMonth = byMonth,
            )
        }
    }
}

@Composable
private fun TotalByMonth(
    modifier: Modifier,
    byMonth: TotalMonthlyState.ByMonth,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(vertical = Normal, horizontal = Large),
            text = byMonth.month,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        )

        Text(
            modifier = Modifier
                .padding(bottom = HalfNormal)
                .padding(horizontal = Large),
            text = stringResource(Resources.string.total),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
        )

        byMonth.byType.total.forEach { value ->
            Text(
                modifier = Modifier.padding(horizontal = Large),
                text = "${value.currencySymbol} ${value.amount}",
                maxLines = 1,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Large)
                .padding(top = Normal),
        ) {
            IncomeExpenseBlock(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(Resources.string.income),
                items = byMonth.byType.income,
                color = IncomeColor,
            )

            IncomeExpenseBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(Resources.string.expense),
                items = byMonth.byType.expense,
                color = ExpenseColor,
            )
        }
    }
}

@Composable
private fun IncomeExpenseBlock(
    modifier: Modifier,
    title: String,
    items: List<TotalMonthlyState.ByType.Value>,
    color: Color,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier,
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Column(
            modifier = Modifier
                .padding(top = HalfNormal),
        ) {
            items.forEach { item ->
                Row {
                    Text(
                        color = color,
                        text = "${item.currencySymbol} ${item.amount}",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}
