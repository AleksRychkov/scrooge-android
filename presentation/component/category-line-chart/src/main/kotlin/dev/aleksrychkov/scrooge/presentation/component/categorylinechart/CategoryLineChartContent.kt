package dev.aleksrychkov.scrooge.presentation.component.categorylinechart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.utils.formatCompactNumber
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.CategoryLineChartComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf.CategoryLineChartState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

private val labelsKey = ExtraStore.Key<List<String>>()
private val bottomAxisFormatter = CartesianValueFormatter { context, value, _ ->
    context.model.extraStore[labelsKey].getOrElse(value.toInt()) { "" }
}
private val startAxisFormatter = CartesianValueFormatter { _, value, _ -> formatCompactNumber(value) }

@Composable
fun CategoryLineChartContent(modifier: Modifier, component: CategoryLineChartComponent) {
    val internal = component as CategoryLineChartComponentInternal
    val state by internal.state.collectAsStateWithLifecycle()
    CategoryLineChartContent(
        modifier = modifier,
        content = state.content,
        currencySymbol = state.filter.currency?.currencySymbol.orEmpty(),
        retry = internal::retry,
    )
}

@Composable
internal fun CategoryLineChartContent(
    modifier: Modifier,
    content: CategoryLineChartState.Content,
    currencySymbol: String,
    retry: () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (content) {
            CategoryLineChartState.Content.Loading -> CircularProgressIndicator()
            CategoryLineChartState.Content.Empty -> Message(R.string.category_chart_no_data)
            CategoryLineChartState.Content.Failure -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Message(R.string.category_chart_error)
                Button(onClick = retry) { Text(stringResource(R.string.category_chart_retry)) }
            }
            is CategoryLineChartState.Content.Data -> ChartWithLegend(content, currencySymbol)
        }
    }
}

@Composable
private fun Message(resource: Int) {
    Text(stringResource(resource), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun ChartWithLegend(
    content: CategoryLineChartState.Content.Data,
    currencySymbol: String,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Large),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            content.series.forEach { series ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        Modifier.size(8.dp).background(Color(series.color), CircleShape),
                    )
                    Text(series.name, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        CategoryChart(Modifier.fillMaxWidth().weight(1f), content, currencySymbol)
    }
}

@Composable
private fun CategoryChart(
    modifier: Modifier,
    content: CategoryLineChartState.Content.Data,
    currencySymbol: String,
) {
    val producer = remember { CartesianChartModelProducer() }
    val scrollState = rememberVicoScrollState(scrollEnabled = true)
    LaunchedEffect(content) {
        producer.runTransaction {
            columnModel { content.series.forEach { series(it.amounts) } }
            extras { it[labelsKey] = content.labels }
        }
        delay(SCROLL_AFTER_LAYOUT_DELAY_MILLIS)
        scrollState.scroll(Scroll.Absolute.End)
    }
    val columns = content.series.map { series ->
        rememberLineComponent(
            fill = Fill(Color(series.color)),
            thickness = CATEGORY_COLUMN_WIDTH,
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
        modifier = modifier.padding(Large),
        modelProducer = producer,
        scrollState = scrollState,
        animateIn = false,
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(columns),
            ),
            startAxis = VerticalAxis.rememberStart(valueFormatter = startAxisFormatter),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = bottomAxisFormatter),
            marker = marker,
        ),
    )
}

private const val SCROLL_AFTER_LAYOUT_DELAY_MILLIS = 100L
private val CATEGORY_COLUMN_WIDTH = 24.dp

@Preview
@Composable
@Suppress("MagicNumber", "UnusedPrivateMember")
private fun CategoryLineChartPreview() {
    AppTheme {
        CategoryLineChartContent(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            content = CategoryLineChartState.Content.Data(
                labels = persistentListOf("Jan 2026", "Feb 2026", "Mar 2026"),
                series = persistentListOf(
                    CategoryLineChartState.Series("Food", 0xFFE57373.toInt(), persistentListOf(30.0, 45.0, 20.0)),
                    CategoryLineChartState.Series("Taxi", 0xFF64B5F6.toInt(), persistentListOf(10.0, 25.0, 40.0)),
                ),
            ),
            currencySymbol = "₽",
            retry = {},
        )
    }
}
