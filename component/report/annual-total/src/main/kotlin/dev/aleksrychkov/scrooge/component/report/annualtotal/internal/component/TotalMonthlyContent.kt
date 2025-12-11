package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyState
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun TotalMonthlyContent(
    modifier: Modifier,
    listState: LazyListState? = null,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    headerItem: @Composable (() -> Unit)? = null,
    component: TotalMonthlyComponent,
) {
    val state by component.state.collectAsStateWithLifecycle()

    TotalMonthlyContent(
        modifier = modifier,
        listState = listState,
        headerItem = headerItem,
        paddingTop = paddingTop,
        paddingBottom = paddingBottom,
        state = state,
    )
}

@Composable
private fun TotalMonthlyContent(
    modifier: Modifier,
    listState: LazyListState? = null,
    headerItem: @Composable (() -> Unit)? = null,
    paddingTop: Dp,
    paddingBottom: Dp,
    state: TotalMonthlyState,
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
                modifier = Modifier.fillMaxWidth(),
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
        modifier = modifier
            .padding(horizontal = Large),
    ) {
        Text(
            modifier = Modifier.padding(vertical = Normal),
            text = byMonth.month,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = HalfNormal),
                    text = stringResource(Resources.string.total),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                )

                byMonth.byType.total.forEach { value ->
                    Text(
                        text = "${value.currencySymbol} ${value.amount}",
                        maxLines = 1,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Small),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = HalfNormal),
                    text = stringResource(Resources.string.income),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                )

                byMonth.byType.income.forEach { value ->
                    Text(
                        text = "${value.currencySymbol} ${value.amount}",
                        color = IncomeColor,
                        maxLines = 1,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Small),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = HalfNormal),
                    text = stringResource(Resources.string.expense),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                )

                byMonth.byType.expense.forEach { value ->
                    Text(
                        text = "${value.currencySymbol} ${value.amount}",
                        color = ExpenseColor,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
