@file:Suppress("All")

package dev.aleksrychkov.scrooge.presentation.component.periodbalance

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LayeredComponent
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.theme.primaryBlue
import dev.aleksrychkov.scrooge.presentation.component.periodbalance.internal.PeriodBalanceComponentInternal
import kotlinx.coroutines.delay

@Composable
fun PeriodBalanceContent(
    modifier: Modifier,
    component: PeriodBalanceComponent
) {
    PeriodBalanceContent(
        modifier = modifier,
        component = component as PeriodBalanceComponentInternal,
    )
}

private val x = (2000..2020).toList()
private val y = listOf<Number>(
    1, 2, 3, 4, 2, 6, 7, 8, 1, 10, 11, 90, 13, 14, 15, 1, 17, 18, 50, 3000
)

private val StartAxisValueFormatter =
    CartesianValueFormatter { _, value, _ -> "$value" }
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(suffix = "R")

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
private fun PeriodBalanceContent(
    modifier: Modifier,
    component: PeriodBalanceComponentInternal
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineModel { series(y) }
            extras { it[BottomAxisLabelKey] = x.map { it.toString() } }
        }
    }
    ComposeElectricCarSales(modelProducer, modifier)
}

@Composable
private fun ComposeElectricCarSales(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val lineColor = primaryBlue
    val scrollState = rememberVicoScrollState(scrollEnabled = true)
    LaunchedEffect(modelProducer) {
        delay(1000)
        scrollState.scroll(Scroll.Absolute.End)
    }
    CartesianChartHost(
        modifier = modifier.padding(all = Large),
        modelProducer = modelProducer,
        scrollState = scrollState,
        animateIn = false,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
                        areaFill =
                        LineCartesianLayer.AreaFill.single(
                            Fill(
                                Brush.verticalGradient(
                                    listOf(
                                        lineColor.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            )
                        ),

                        interpolator = LineCartesianLayer.Interpolator.catmullRom(),
                    )
                ),
                pointSpacing = 60.dp,
            ),
            startAxis = VerticalAxis.rememberStart(valueFormatter = StartAxisValueFormatter),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
            ),
            marker = rememberMarker(MarkerValueFormatter),
            layerPadding = {
                CartesianLayerPadding(
                    scalableStart = Normal2X,
                    scalableEnd = Normal2X
                )
            },
        ),
    )
}

@Composable
internal fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = MarkerCornerBasedShape(CircleShape)
    val labelBackground = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.background),
        shape = labelBackgroundShape,
        strokeFill = Fill(MaterialTheme.colorScheme.outline),
        strokeThickness = 1.dp,
    )
    val label = rememberTextComponent(
        style = MaterialTheme.typography.labelSmall,
        padding = Insets(Medium, Small),
        background = labelBackground,
    )
    val indicatorFrontComponent =
        rememberShapeComponent(Fill(MaterialTheme.colorScheme.surface), CircleShape)
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator =
        if (showIndicator) {
            { color ->
                LayeredComponent(
                    back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CircleShape),
                    front =
                    LayeredComponent(
                        back = ShapeComponent(fill = Fill(color), shape = CircleShape),
                        front = indicatorFrontComponent,
                        padding = Insets(Small),
                    ),
                    padding = Insets(Medium),
                )
            }
        } else {
            null
        },
        indicatorSize = Large2X,
        guideline = guideline,
    )
}
