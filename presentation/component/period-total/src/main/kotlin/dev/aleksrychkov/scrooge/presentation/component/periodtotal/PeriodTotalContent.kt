package dev.aleksrychkov.scrooge.presentation.component.periodtotal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.PeriodTotalComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalState
import kotlinx.collections.immutable.persistentListOf
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun PeriodTotalContent(
    modifier: Modifier,
    component: PeriodTotalComponent,
) {
    PeriodTotalContent(
        modifier = modifier,
        component = component as PeriodTotalComponentInternal,
    )
}

@Composable
private fun PeriodTotalContent(
    modifier: Modifier,
    component: PeriodTotalComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        state = state,
        openCategoryTotal = component::openCategoryTotal,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: PeriodTotalState,
    openCategoryTotal: () -> Unit,
) {
    TotalContent(
        modifier = modifier,
        data = state.data,
        openCategoryTotal = openCategoryTotal,
    )
}

@Composable
private fun TotalContent(
    modifier: Modifier,
    data: PeriodTotalState.ByType,
    openCategoryTotal: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .animateContentSize()
            .debounceClickable {
                openCategoryTotal()
            }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = Large)
                .padding(top = Large),
            text = stringResource(Resources.string.total),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 60.dp,
                )
                .padding(horizontal = Large)
                .padding(vertical = Normal),
        ) {
            data.total.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = item.amount,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.width(Normal))
                    Text(
                        text = item.currencySymbol,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 80.dp,
                )
                .background(MaterialTheme.colorScheme.secondary)
                .padding(horizontal = Large)
                .padding(bottom = Normal),
        ) {
            IncomeExpenseBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(Resources.string.incomes),
                items = data.income,
                color = IncomeColor,
            )

            IncomeExpenseBlock(
                modifier = Modifier.weight(1f),
                title = stringResource(Resources.string.expenses),
                items = data.expense,
                color = ExpenseColor,
            )
        }
    }
}

@Composable
private fun IncomeExpenseBlock(
    modifier: Modifier,
    title: String,
    items: List<PeriodTotalState.ByType.Value>,
    color: Color,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(vertical = Normal),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Column {
            items.forEach { item ->
                Row {
                    Text(
                        color = color,
                        text = item.amount,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.width(Medium))
                    Text(
                        color = color,
                        text = item.currencySymbol,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun TotalContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TotalContent(
                modifier = Modifier.fillMaxWidth(),
                openCategoryTotal = {},
                data = PeriodTotalState.ByType(
                    income = persistentListOf(
                        PeriodTotalState.ByType.Value(
                            currencySymbol = "₽",
                            amount = "123,00"
                        ),
                        PeriodTotalState.ByType.Value(
                            currencySymbol = "€",
                            amount = "123,00"
                        )
                    ),
                    expense = persistentListOf(
                        PeriodTotalState.ByType.Value(
                            currencySymbol = "₽",
                            amount = "123,00"
                        ),
                        PeriodTotalState.ByType.Value(
                            currencySymbol = "€",
                            amount = "123,00"
                        )
                    ),
                    total = persistentListOf(
                        PeriodTotalState.ByType.Value(
                            currencySymbol = "₽",
                            amount = "0,00"
                        ),
                        PeriodTotalState.ByType.Value(
                            currencySymbol = "€",
                            amount = "0,00"
                        )
                    ),
                ),
            )
        }
    }
}
