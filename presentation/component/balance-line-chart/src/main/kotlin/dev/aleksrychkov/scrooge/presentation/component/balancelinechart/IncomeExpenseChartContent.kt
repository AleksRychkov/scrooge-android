package dev.aleksrychkov.scrooge.presentation.component.balancelinechart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.utils.formatCompactNumber
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.IncomeExpenseChartComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.IncomeExpenseChartState
import kotlinx.coroutines.delay

private val incomeExpenseLabelsKey = ExtraStore.Key<List<String>>()
private val incomeExpenseBottomAxisFormatter = CartesianValueFormatter { context, value, _ ->
    context.model.extraStore[incomeExpenseLabelsKey].getOrElse(value.toInt()) { "" }
}
private val incomeExpenseStartAxisFormatter =
    CartesianValueFormatter { _, value, _ -> formatCompactNumber(value) }

@Composable
fun IncomeExpenseChartContent(
    modifier: Modifier,
    component: IncomeExpenseChartComponent,
) {
    val internal = component as IncomeExpenseChartComponentInternal
    val state by internal.state.collectAsStateWithLifecycle()
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (val content = state.content) {
            IncomeExpenseChartState.Content.Loading -> CircularProgressIndicator()
            IncomeExpenseChartState.Content.Empty -> Message(R.string.income_expense_chart_no_data)
            IncomeExpenseChartState.Content.Failure -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Message(R.string.income_expense_chart_error)
                Button(onClick = internal::retry) {
                    Text(stringResource(R.string.balance_chart_retry))
                }
            }
            is IncomeExpenseChartState.Content.Data -> IncomeExpenseChart(
                content = content,
                currencySymbol = state.filter.currency?.currencySymbol.orEmpty(),
            )
        }
    }
}

@Composable
private fun Message(resource: Int) {
    Text(stringResource(resource), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun IncomeExpenseChart(
    content: IncomeExpenseChartState.Content.Data,
    currencySymbol: String,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Large),
            horizontalArrangement = Arrangement.spacedBy(Large),
        ) {
            LegendItem(IncomeColor, stringResource(R.string.income_chart_legend))
            LegendItem(ExpenseColor, stringResource(R.string.expense_chart_legend))
        }
        val producer = remember { CartesianChartModelProducer() }
        val scrollState = rememberVicoScrollState(scrollEnabled = true)
        LaunchedEffect(content) {
            producer.runTransaction {
                lineModel {
                    series(content.income)
                    series(content.expense)
                }
                extras { it[incomeExpenseLabelsKey] = content.labels }
            }
            delay(SCROLL_AFTER_LAYOUT_DELAY_MILLIS)
            scrollState.scroll(Scroll.Absolute.End)
        }
        val incomeLine = LineCartesianLayer.rememberLine(
            fill = LineCartesianLayer.LineFill.single(Fill(IncomeColor)),
        )
        val expenseLine = LineCartesianLayer.rememberLine(
            fill = LineCartesianLayer.LineFill.single(Fill(ExpenseColor)),
        )
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
            modifier = Modifier.fillMaxWidth().weight(1f).padding(Large),
            modelProducer = producer,
            scrollState = scrollState,
            animateIn = false,
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        incomeLine,
                        expenseLine,
                    ),
                    pointSpacing = 56.dp,
                ),
                startAxis = VerticalAxis.rememberStart(
                    valueFormatter = incomeExpenseStartAxisFormatter,
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = incomeExpenseBottomAxisFormatter,
                ),
                marker = marker,
            ),
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

private const val SCROLL_AFTER_LAYOUT_DELAY_MILLIS = 100L
