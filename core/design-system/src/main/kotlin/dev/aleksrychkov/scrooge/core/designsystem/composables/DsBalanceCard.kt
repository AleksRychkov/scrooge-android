package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun DsBalanceCard(
    modifier: Modifier,
    data: DsBalanceData,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = Medium),
        shape = ShapeDefaults.Large.copy(
            topStart = CornerSize(0.dp),
            topEnd = CornerSize(0.dp),
            bottomStart = CornerSize(Large2X),
            bottomEnd = CornerSize(Large2X),
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = Large),
                text = stringResource(Resources.string.total),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Large)
                    .padding(vertical = Normal)
                    .animateContentSize(),
            ) {
                data.total.forEach { item ->
                    Row {
                        Text(
                            modifier = Modifier.width(Normal2X),
                            text = item.currencySymbol,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = Large)
                    .padding(bottom = Normal)
                    .animateContentSize(),
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
    items: List<DsBalanceData.Total>,
    color: Color,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(Normal),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Column(
            modifier = Modifier.padding(horizontal = Large),
        ) {
            items.forEach { item ->
                Row {
                    Text(
                        modifier = Modifier.width(Normal2X),
                        color = color,
                        text = item.currencySymbol,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = item.value,
                        color = color,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Immutable
data class DsBalanceData(
    val income: ImmutableList<Total> = persistentListOf(),
    val expense: ImmutableList<Total> = persistentListOf(),
    val total: ImmutableList<Total> = persistentListOf(),
) {
    @Immutable
    data class Total(
        val currencySymbol: String,
        val value: String,
    )
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun DsBalanceCardPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DsBalanceCard(
                modifier = Modifier.fillMaxWidth(),
                data = DsBalanceData(
                    income = persistentListOf(
                        DsBalanceData.Total(
                            currencySymbol = "₽",
                            value = "123,00"
                        ),
                        DsBalanceData.Total(
                            currencySymbol = "€",
                            value = "123,00"
                        )
                    ),
                    expense = persistentListOf(
                        DsBalanceData.Total(
                            currencySymbol = "₽",
                            value = "123,00"
                        ),
                        DsBalanceData.Total(
                            currencySymbol = "€",
                            value = "123,00"
                        )
                    ),
                    total = persistentListOf(
                        DsBalanceData.Total(
                            currencySymbol = "₽",
                            value = "0,00"
                        ),
                        DsBalanceData.Total(
                            currencySymbol = "€",
                            value = "0,00"
                        )
                    ),
                ),
            )
        }
    }
}
