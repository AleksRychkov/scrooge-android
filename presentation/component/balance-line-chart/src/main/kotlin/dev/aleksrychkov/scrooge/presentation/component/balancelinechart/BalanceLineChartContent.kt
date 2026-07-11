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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.primaryBlue
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.BalanceLineChartComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceLineChartState
import kotlinx.collections.immutable.persistentListOf

private val labelsKey = ExtraStore.Key<List<String>>()
private val bottomAxisFormatter = CartesianValueFormatter { context, value, _ ->
    context.model.extraStore[labelsKey].getOrElse(value.toInt()) { "" }
}

@Composable
fun BalanceLineChartContent(
    modifier: Modifier,
    component: BalanceLineChartComponent,
) {
    val internal = component as BalanceLineChartComponentInternal
    val state by internal.state.collectAsStateWithLifecycle()
    BalanceLineChartContent(modifier, state.content, internal::retry)
}

@Composable
internal fun BalanceLineChartContent(
    modifier: Modifier,
    content: BalanceLineChartState.Content,
    retry: () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (content) {
            BalanceLineChartState.Content.Loading -> CircularProgressIndicator()
            BalanceLineChartState.Content.Empty -> Message(R.string.balance_chart_no_data)
            BalanceLineChartState.Content.Failure -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Message(R.string.balance_chart_error)
                Button(onClick = retry) { Text(stringResource(R.string.balance_chart_retry)) }
            }
            is BalanceLineChartState.Content.Data -> Chart(content)
        }
    }
}

@Composable
private fun Message(resource: Int) {
    Text(text = stringResource(resource), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun Chart(content: BalanceLineChartState.Content.Data) {
    val producer = remember { CartesianChartModelProducer() }
    LaunchedEffect(content) {
        producer.runTransaction {
            lineModel { series(content.amounts) }
            extras { it[labelsKey] = content.labels }
        }
    }
    val lineColor = primaryBlue
    CartesianChartHost(
        modifier = Modifier.fillMaxSize().padding(Large),
        modelProducer = producer,
        animateIn = false,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
                        areaFill = LineCartesianLayer.AreaFill.single(
                            Fill(Brush.verticalGradient(listOf(lineColor.copy(alpha = 0.35f), Color.Transparent))),
                        ),
                    ),
                ),
                pointSpacing = 56.dp,
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = bottomAxisFormatter),
        ),
    )
}

@Preview
@Composable
@Suppress("MagicNumber", "UnusedPrivateMember")
private fun BalanceLineChartPreview() {
    AppTheme {
        BalanceLineChartContent(
            modifier = Modifier.fillMaxSize(),
            content = BalanceLineChartState.Content.Data(
                labels = persistentListOf("Jan 2026", "Feb 2026", "Mar 2026"),
                amounts = persistentListOf(100, -30, 80),
            ),
            retry = {},
        )
    }
}
