@file:Suppress("LongMethod", "MagicNumber")

package dev.aleksrychkov.scrooge.component.report.root.internal.composables

import android.content.res.Configuration
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun DonutChart(
    modifier: Modifier = Modifier,
    segments: ImmutableList<DonutChartSegment>,
    animateOnSegmentChange: Boolean = false,
    animationDuration: Duration = 1000.milliseconds,
    strokeWidth: Dp = 36.dp,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = 14.sp,
) {
    val sweepAngles: List<Float>

    if (animateOnSegmentChange) {
        val animatedSweeps = remember(segments) {
            segments.map { Animatable(0f) }
        }
        LaunchedEffect(segments) {
            segments.forEachIndexed { i, segment ->
                animatedSweeps[i].animateTo(
                    targetValue = segment.percentage * 360f,
                    animationSpec = tween(
                        durationMillis = (animationDuration.inWholeMilliseconds * segment.percentage).roundToInt(),
                        easing = LinearEasing,
                    )
                )
            }
        }
        sweepAngles = animatedSweeps.map { it.value }.toImmutableList()
    } else {
        sweepAngles = segments.map { it.percentage * 360 }.toImmutableList()
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        DonutArcsNeonBlur(
            segments = segments,
            sweepAngles = sweepAngles,
            strokeWidth = strokeWidth,
        )
        DonutLabels(
            segments = segments,
            sweepAngles = sweepAngles,
            strokeWidth = strokeWidth,
            textColor = textColor,
            fontSize = fontSize,
        )
    }
}

@Composable
private fun DonutLabels(
    segments: ImmutableList<DonutChartSegment>,
    sweepAngles: ImmutableList<Float>,
    strokeWidth: Dp,
    textColor: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
) {
    val density = LocalDensity.current
    val strokePx = with(density) { strokeWidth.toPx() }
    val fontSizeInPx = with(density) { fontSize.toPx() }

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val modifier = if (isLandscape) {
        Modifier.fillMaxHeight()
    } else {
        Modifier.fillMaxWidth()
    }
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(strokeWidth * 1.5f)
    ) {
        var startAngle = -90f
        segments.forEachIndexed { i, segment ->

            val sweep = sweepAngles[i]
            val midAngle = startAngle + sweep / 2f

            val radius = size.minDimension / 2f + strokePx
            val x = center.x + cos(Math.toRadians(midAngle.toDouble())).toFloat() * radius
            val y = center.y + sin(Math.toRadians(midAngle.toDouble())).toFloat() * radius

            val text = "${(segment.percentage * 100).roundToInt()}%"

            val txtSize = if (segment.percentage < 0.05f) {
                fontSizeInPx / 2f
            } else {
                fontSizeInPx
            }
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    color = textColor.toArgb()
                    textSize = txtSize
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                }
                canvas.nativeCanvas.drawText(text, x, y, paint)
            }
            startAngle += sweep
        }
    }
}

@Composable
private fun DonutArcsNeonBlur(
    segments: ImmutableList<DonutChartSegment>,
    sweepAngles: ImmutableList<Float>,
    strokeWidth: Dp,
) {
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val modifier = if (isLandscape) {
        Modifier.fillMaxHeight()
    } else {
        Modifier.fillMaxWidth()
    }
    val mainGlowColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
    val baseArcColor = MaterialTheme.colorScheme.tertiary
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(strokeWidth * 1.5f)
    ) {
        val strokePx = strokeWidth.toPx()
        val baseStroke = Stroke(
            width = strokePx,
            cap = StrokeCap.Round,
        )

        var startAngle = -90f

        val baseArcRect = Rect(
            left = -10f,
            top = 10f,
            right = size.width + 10f,
            bottom = size.height + 10f,
        )
        val baseArcPaint = Paint().apply {
            color = mainGlowColor.toArgb()
            style = Paint.Style.STROKE
            this.strokeWidth = baseStroke.width
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            setMaskFilter(
                BlurMaskFilter(
                    150f,
                    BlurMaskFilter.Blur.NORMAL
                )
            )
        }
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawArc(
                baseArcRect.left,
                baseArcRect.top,
                baseArcRect.right,
                baseArcRect.bottom,
                startAngle,
                360f,
                false,
                baseArcPaint,
            )
        }

        drawArc(
            color = baseArcColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = baseStroke
        )

        val rect = Rect(
            left = -5f,
            top = 5f,
            right = size.width + 5f,
            bottom = size.height + 5f,
        )
        val glowSegmentPaint = Paint().apply {
            color = Color.Black.copy(alpha = 0.2f).toArgb()
            style = Paint.Style.STROKE
            this.strokeWidth = baseStroke.width
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            setMaskFilter(
                BlurMaskFilter(
                    20f,
                    BlurMaskFilter.Blur.NORMAL
                )
            )
        }
        segments.forEachIndexed { i, segment ->
            val sweep = sweepAngles[i]
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawArc(
                    rect.left,
                    rect.top,
                    rect.right,
                    rect.bottom,
                    startAngle,
                    sweep,
                    false,
                    glowSegmentPaint
                )
            }

            // Draw solid arc on top
            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                style = baseStroke
            )

            startAngle += sweep
        }

        // Draw first arc to hide shadow of last segment
        val minimalSweepAngle = 6f
        val drawLastArc by derivedStateOf { segments[0].percentage * 360f >= minimalSweepAngle }
        if (drawLastArc) {
            drawArc(
                color = segments[0].color,
                startAngle = startAngle,
                sweepAngle = minimalSweepAngle,
                useCenter = false,
                style = baseStroke
            )
        }
    }
}

@Immutable
internal data class DonutChartSegment(
    val percentage: Float,
    val color: Color,
)

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun DonutChartPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            val segments = persistentListOf(
                DonutChartSegment(0.67f, Color(0xFFFFB54B)),
                DonutChartSegment(0.18f, Color(0xFFE53935)),
                DonutChartSegment(0.05f, Color(0xFF5E35B1)),
                DonutChartSegment(0.02f, Color(0xFFFFA48C)),
                DonutChartSegment(0.02f, Color(0xFFC0CA33)),
                DonutChartSegment(0.02f, Color(0xFF3949AB)),
                DonutChartSegment(0.02f, Color(0xFF00ACC1)),
                DonutChartSegment(0.01f, Color(0xFFFFB300)),
                DonutChartSegment(0.01f, Color(0xFF00897B)),
            )

            DonutChart(
                segments = segments,
                strokeWidth = 40.dp,
                fontSize = 15.sp,
            )
        }
    }
}
