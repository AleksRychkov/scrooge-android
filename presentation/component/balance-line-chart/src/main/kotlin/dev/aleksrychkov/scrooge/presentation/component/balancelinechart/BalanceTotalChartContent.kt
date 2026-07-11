package dev.aleksrychkov.scrooge.presentation.component.balancelinechart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.BalanceTotalChartComponentInternal

@Composable
fun BalanceTotalChartContent(
    modifier: Modifier,
    component: BalanceTotalChartComponent,
) {
    val internal = component as BalanceTotalChartComponentInternal
    val state by internal.state.collectAsStateWithLifecycle()
    BalanceLineChartContent(
        modifier = modifier,
        content = state.content,
        currencySymbol = state.filter.currency?.currencySymbol.orEmpty(),
        retry = internal::retry,
    )
}
