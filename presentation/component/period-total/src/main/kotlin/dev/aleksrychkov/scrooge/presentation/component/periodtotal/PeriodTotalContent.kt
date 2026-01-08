package dev.aleksrychkov.scrooge.presentation.component.periodtotal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.PeriodTotalComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalState
import kotlinx.collections.immutable.persistentListOf
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun PeriodTotalContent(
    modifier: Modifier,
    component: PeriodTotalComponent,
    elevation: Dp = Medium,
) {
    PeriodTotalContent(
        modifier = modifier,
        component = component as PeriodTotalComponentInternal,
        elevation = elevation,
    )
}

@Composable
private fun PeriodTotalContent(
    modifier: Modifier,
    component: PeriodTotalComponentInternal,
    elevation: Dp
) {
    val state by component.state.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        state = state,
        elevation = elevation,
        openCategoryTotal = component::openCategoryTotal,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: PeriodTotalState,
    elevation: Dp,
    openCategoryTotal: () -> Unit,
) {
    TotalContent(
        modifier = modifier,
        data = state.data,
        elevation = elevation,
        openCategoryTotal = openCategoryTotal,
    )
}

@Composable
private fun TotalContent(
    modifier: Modifier,
    data: PeriodTotalState.ByType,
    elevation: Dp,
    openCategoryTotal: () -> Unit,
) {
    Card(
        modifier = modifier
            .wrapContentSize()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = ShapeDefaults.Large.copy(
            topStart = CornerSize(0.dp),
            topEnd = CornerSize(0.dp),
            bottomStart = CornerSize(Large2X),
            bottomEnd = CornerSize(Large2X),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .debounceClickable {
                    openCategoryTotal()
                }
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = Large)
                    .padding(top = Small),
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
                    Row {
                        Text(
                            modifier = Modifier.width(Normal2X),
                            text = item.currencySymbol,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = item.amount,
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
                    title = stringResource(Resources.string.income),
                    items = data.income,
                    color = IncomeColor,
                )

                IncomeExpenseBlock(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Resources.string.expense),
                    items = data.expense,
                    color = ExpenseColor,
                )
            }
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
                        modifier = Modifier.defaultMinSize(minWidth = Large),
                        color = color,
                        text = item.currencySymbol,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        color = color,
                        text = item.amount,
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
                elevation = Medium,
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
