package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceItem
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceState
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import kotlinx.collections.immutable.ImmutableList
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun BalanceContent(
    modifier: Modifier,
    component: BalanceComponent,
) {
    val state by component.state.collectAsStateWithLifecycle()
    BalanceContent(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun BalanceContent(
    modifier: Modifier,
    state: BalanceState
) {
    Column(
        modifier = modifier
            .padding(Large)
            .background(
                shape = CardDefaults.shape,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
            .clip(CardDefaults.shape)
            .padding(Normal)
            .animateContentSize(),
    ) {
        BalanceItem(
            icon = Resources.drawable.ic_trending_up_24px,
            title = Resources.string.income,
            isLoading = state.isLoading,
            values = state.income,
            color = IncomeColor,
        )
        Spacer(modifier = Modifier.height(Normal2X))

        BalanceItem(
            icon = Resources.drawable.ic_trending_down_24px,
            title = Resources.string.expense,
            isLoading = state.isLoading,
            values = state.expense,
            color = ExpenseColor,
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal)
        )

        BalanceItem(
            icon = Resources.drawable.ic_balance_24px,
            title = Resources.string.total,
            isLoading = state.isLoading,
            values = state.total,
            color = Color.Unspecified,
        )

        AnimatedVisibility(
            visible = !state.isLoading && state.total.isNotEmpty()
        ) {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Normal)
                )
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(Resources.string.details),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun BalanceItem(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    isLoading: Boolean,
    color: Color,
    values: ImmutableList<BalanceItem>,
) {
    Row(
        verticalAlignment = Alignment.Top,
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
                modifier = Modifier.size(18.dp)
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        AnimatedVisibility(
            visible = !isLoading
        ) {
            Column {
                values.forEach { item ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = item.value + " ${item.currency.currencySymbol}",
                        textAlign = TextAlign.End,
                        color = color,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }
    }
}
