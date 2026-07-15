package dev.aleksrychkov.scrooge.presentation.component.balancelinechart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.utils.formatCompactNumber
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.BalanceTotalChartComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceTotalChartState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

private val labelsKey = ExtraStore.Key<List<String>>()
private val bottomAxisFormatter = CartesianValueFormatter { context, value, _ ->
    context.model.extraStore[labelsKey].getOrElse(value.toInt()) { "" }
}
private val startAxisFormatter = CartesianValueFormatter { _, value, _ -> formatCompactNumber(value) }

@Composable
fun BalanceTotalChartContent(
    modifier: Modifier,
    component: BalanceTotalChartComponent,
) {
    val internal = component as BalanceTotalChartComponentInternal
    val state by internal.state.collectAsStateWithLifecycle()
    BalanceTotalChartContent(
        modifier = modifier,
        content = state.content,
        currencySymbol = state.filter.currency?.currencySymbol.orEmpty(),
        retry = internal::retry,
    )
}

@Composable
internal fun BalanceTotalChartContent(
    modifier: Modifier,
    content: BalanceTotalChartState.Content,
    currencySymbol: String,
    retry: () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (content) {
            BalanceTotalChartState.Content.Loading -> CircularProgressIndicator()
            BalanceTotalChartState.Content.Empty -> Message(R.string.balance_total_chart_no_data)
            BalanceTotalChartState.Content.Failure -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Message(R.string.balance_total_chart_error)
                Button(onClick = retry) { Text(stringResource(R.string.balance_total_chart_retry)) }
            }
            is BalanceTotalChartState.Content.Data -> Chart(content, currencySymbol)
        }
    }
}

@Composable
private fun Message(resource: Int) {
    Text(text = stringResource(resource), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun Chart(
    content: BalanceTotalChartState.Content.Data,
    currencySymbol: String,
) {
    val producer = remember { CartesianChartModelProducer() }
    val scrollState = rememberVicoScrollState(scrollEnabled = true)
    LaunchedEffect(content) {
        producer.runTransaction {
            columnModel { series(content.amounts) }
            extras { it[labelsKey] = content.labels }
        }
        delay(SCROLL_AFTER_LAYOUT_DELAY_MILLIS)
        scrollState.scroll(Scroll.Absolute.End)
    }
    val incomeColumn = rememberLineComponent(
        fill = Fill(IncomeColor),
        thickness = BALANCE_COLUMN_WIDTH,
    )
    val expenseColumn = rememberLineComponent(
        fill = Fill(ExpenseColor),
        thickness = BALANCE_COLUMN_WIDTH,
    )
    val columnProvider = remember(incomeColumn, expenseColumn) {
        BalanceColumnProvider(
            incomeColumn = incomeColumn,
            expenseColumn = expenseColumn,
        )
    }
    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(style = MaterialTheme.typography.labelMedium),
        valueFormatter = DefaultCartesianMarker.ValueFormatter.default(
            decimalCount = 0,
            decimalSeparator = ",",
            thousandsSeparator = " ",
            suffix = " $currencySymbol",
        ),
    )
    CartesianChartHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = Large, end = Large, bottom = Large),
        modelProducer = producer,
        scrollState = scrollState,
        animateIn = false,
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(columnProvider = columnProvider),
            startAxis = VerticalAxis.rememberStart(valueFormatter = startAxisFormatter),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = bottomAxisFormatter),
            marker = marker,
        ),
    )
}

private const val SCROLL_AFTER_LAYOUT_DELAY_MILLIS = 100L
private val BALANCE_COLUMN_WIDTH = 24.dp

private class BalanceColumnProvider(
    private val incomeColumn: LineComponent,
    private val expenseColumn: LineComponent,
) : ColumnCartesianLayer.ColumnProvider {
    override fun getColumn(
        entry: com.patrykandpatrick.vico.compose.cartesian.data.ColumnCartesianLayerModel.Entry,
        extraStore: ExtraStore,
    ): LineComponent = if (entry.y < 0) expenseColumn else incomeColumn

    override fun getWidestSeriesColumn(
        seriesKey: Any,
        seriesIndex: Int,
        extraStore: ExtraStore,
    ): LineComponent = incomeColumn
}

@Preview
@Composable
@Suppress("MagicNumber", "UnusedPrivateMember")
private fun BalanceTotalChartPreview() {
    AppTheme {
        BalanceTotalChartContent(
            modifier = Modifier.fillMaxSize(),
            content = BalanceTotalChartState.Content.Data(
                labels = persistentListOf("01/26", "02/26", "03/26"),
                amounts = persistentListOf(100.0, -30.0, 80.0),
            ),
            currencySymbol = "₽",
            retry = {},
        )
    }
}
