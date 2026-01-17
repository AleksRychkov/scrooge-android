@file:Suppress("MagicNumber")

package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.composables

import android.content.res.Configuration
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun PieChart(
    modifier: Modifier = Modifier,
    segments: ImmutableList<PieChartSegment>,
    animateOnSegmentChange: Boolean = false,
    animationDuration: Duration = 1000.milliseconds,
    strokeWidth: Dp = 28.dp,
    textSize: TextUnit = 12.sp,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    backgroundGlowColor: Color = Color.Gray.copy(alpha = 0.5f),
) {
    val fixedSweepAngles = remember(segments) {
        segments.map { it.percentage * 360 }.toImmutableList()
    }
    val sweepAngles: List<Float>

    if (animateOnSegmentChange) {
        val animatedSweeps = remember(segments) {
            segments.map { Animatable(0f) }
        }
        LaunchedEffect(segments) {
            coroutineScope {
                segments.forEachIndexed { i, segment ->
                    launch {
                        animatedSweeps[i].animateTo(
                            targetValue = segment.percentage * 360f,
                            animationSpec = tween(
                                durationMillis = (animationDuration.inWholeMilliseconds).toInt(),
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            }
        }
        sweepAngles = animatedSweeps.map { it.value }.toImmutableList()
    } else {
        sweepAngles = fixedSweepAngles
    }
    val startAngles = remember(fixedSweepAngles) {
        val angles = mutableListOf<Float>()
        var currentAngle = -90f
        for (sweep in fixedSweepAngles) {
            angles.add(currentAngle)
            currentAngle += sweep
        }
        angles.toImmutableList()
    }
    PieChart(
        modifier = modifier,
        segments = segments,
        sweepAngles = sweepAngles,
        startAngles = startAngles,
        strokeWidth = strokeWidth,
        textSize = textSize,
        textColor = textColor,
        backgroundColor = backgroundColor,
        backgroundGlowColor = backgroundGlowColor
    )
}

@Composable
private fun PieChart(
    modifier: Modifier = Modifier,
    segments: ImmutableList<PieChartSegment>,
    sweepAngles: ImmutableList<Float>,
    startAngles: ImmutableList<Float>,
    strokeWidth: Dp,
    textSize: TextUnit,
    textColor: Color,
    backgroundColor: Color,
    backgroundGlowColor: Color,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val localConfiguration = LocalConfiguration.current
        val orientation = localConfiguration.orientation
        val modifier = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Modifier.fillMaxHeight()
        } else {
            Modifier.fillMaxWidth()
        }
            .aspectRatio(1f)
            .padding(strokeWidth * 1.5f)
        Background(
            modifier = modifier,
            strokeWidth = strokeWidth,
            color = backgroundColor,
            glowColor = backgroundGlowColor,
        )
        if (segments.isNotEmpty()) {
            Segments(
                modifier = modifier,
                strokeWidth = strokeWidth,
                segments = segments,
                sweepAngles = sweepAngles,
                startAngles = startAngles,
                textSize = textSize,
                textColor = textColor,
            )
        }
    }
}

@Composable
private fun Background(
    modifier: Modifier,
    strokeWidth: Dp,
    color: Color,
    glowColor: Color,
) {
    Canvas(
        modifier = modifier
    ) {
        val stroke = Stroke(width = strokeWidth.toPx())
        val glowRect = Rect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
        )
        val glowPaint = Paint().apply {
            this.color = glowColor.toArgb()
            this.style = Paint.Style.STROKE
            this.strokeWidth = stroke.width
            this.isAntiAlias = true
            this.setMaskFilter(BlurMaskFilter(75f, BlurMaskFilter.Blur.NORMAL))
        }
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawArc(
                glowRect.left,
                glowRect.top,
                glowRect.right,
                glowRect.bottom,
                0f,
                360f,
                false,
                glowPaint,
            )
        }
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke
        )
    }
}

@Composable
private fun Segments(
    modifier: Modifier,
    strokeWidth: Dp,
    segments: ImmutableList<PieChartSegment>,
    startAngles: ImmutableList<Float>,
    sweepAngles: ImmutableList<Float>,
    textSize: TextUnit,
    textColor: Color,
) {
    val painters = segments
        .map { rememberVectorPainter(it.icon) }
        .toImmutableList()

    Canvas(modifier = modifier) {
        val strokeWidthPx = strokeWidth.toPx()

        // draw segments
        drawSegments(
            segments = segments,
            sweepAngles = sweepAngles,
            startAngles = startAngles,
            strokeWidthPx = strokeWidthPx,
        )

        // draw segment's caps
        drawSegmentCapsAndIcons(
            segments = segments,
            startAngles = startAngles,
            sweepAngles = sweepAngles,
            strokeWidthPx = strokeWidthPx,
            painters = painters,
        )

        // draw percentage labels
        drawSegmentLabels(
            segments = segments,
            startAngles = startAngles,
            sweepAngles = sweepAngles,
            radiusOffset = strokeWidthPx,
            textSize = textSize,
            textColor = textColor,
        )
    }
}

private fun DrawScope.drawSegmentCapsAndIcons(
    segments: ImmutableList<PieChartSegment>,
    startAngles: ImmutableList<Float>,
    sweepAngles: ImmutableList<Float>,
    painters: ImmutableList<Painter>,
    strokeWidthPx: Float,
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2
    for (i in segments.lastIndex downTo 0) {
        val endAngleShadow = startAngles[i] + sweepAngles[i] + 1f
        val angleRadShadow = Math.toRadians(endAngleShadow.toDouble())
        val capCenterShadow = Offset(
            x = center.x + cos(angleRadShadow).toFloat() * radius,
            y = center.y + sin(angleRadShadow).toFloat() * radius
        )

        // Draw shadow behind the cap
        withTransform({
            rotate(
                degrees = endAngleShadow,
                pivot = capCenterShadow,
            )
        }) {
            drawArc(
                color = Color.Black.copy(alpha = 0.1f),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = capCenterShadow - Offset(strokeWidthPx / 2f, strokeWidthPx / 2f),
                size = Size(strokeWidthPx, strokeWidthPx)
            )
        }

        // Draw cap
        val endAngle = startAngles[i] + sweepAngles[i] - 1f
        val angleRad = Math.toRadians(endAngle.toDouble())
        val capCenter = Offset(
            x = center.x + cos(angleRad).toFloat() * radius,
            y = center.y + sin(angleRad).toFloat() * radius
        )
        drawCircle(
            color = Color(segments[i].color),
            radius = strokeWidthPx / 2f,
            center = capCenter,
        )

        // Draw icon
        if (segments[i].percentage > 0.01f) {
            val iconSize = min(size.width, size.height) * 0.08f // example size
            val topLeft = capCenter - Offset(iconSize / 2, iconSize / 2)
            with(painters[i]) {
                withTransform({
                    translate(left = topLeft.x, top = topLeft.y)
                }) {
                    draw(
                        size = Size(iconSize, iconSize),
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawSegments(
    segments: ImmutableList<PieChartSegment>,
    sweepAngles: ImmutableList<Float>,
    startAngles: ImmutableList<Float>,
    strokeWidthPx: Float,
) {
    val stroke = Stroke(
        width = strokeWidthPx,
        cap = StrokeCap.Round,
    )
    for (i in segments.indices) {
        val segment = segments[i]
        val sweep = sweepAngles[i]
        drawArc(
            color = Color(segment.color),
            startAngle = startAngles[i],
            sweepAngle = sweep,
            useCenter = false,
            style = stroke
        )
    }
}

private fun DrawScope.drawSegmentLabels(
    segments: ImmutableList<PieChartSegment>,
    startAngles: ImmutableList<Float>,
    sweepAngles: ImmutableList<Float>,
    radiusOffset: Float,
    textSize: TextUnit,
    textColor: Color
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 + radiusOffset
    val normalPaint = Paint().apply {
        color = textColor.toArgb()
        textAlign = Paint.Align.CENTER
        this.textSize = textSize.toPx()
        isAntiAlias = true
    }
    val smallPaint = Paint(normalPaint).apply {
        this.textSize = textSize.toPx() / 2f
    }

    segments.forEachIndexed { i, segment ->
        if (segment.percentage <= 0.01) return@forEachIndexed

        val midAngle = startAngles[i] + sweepAngles[i] / 2f
        val angleRad = Math.toRadians(midAngle.toDouble())
        val x = center.x + cos(angleRad).toFloat() * radius
        val y = center.y + sin(angleRad).toFloat() * radius

        val paint = if (segment.percentage <= 0.03f) smallPaint else normalPaint

        drawContext.canvas.nativeCanvas.drawText(
            "${(segment.percentage * 100).toInt()}%",
            x,
            y + paint.textSize / 3,
            paint
        )
    }
}

@Immutable
internal data class PieChartSegment(
    val percentage: Float,
    val color: Int,
    val icon: ImageVector,
)

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PieChartPreview() {
    AppTheme(
        useDarkTheme = true
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val icon = UncategorizedIcon.icon
            val segments = persistentListOf(
                PieChartSegment(0.5f, 0xFFFFB54B.toInt(), icon),
                PieChartSegment(0.35f, 0xFFE53935.toInt(), icon),
                PieChartSegment(0.05f, 0xFF5E35B1.toInt(), icon),
                PieChartSegment(0.02f, 0xFFFFA48C.toInt(), icon),
                PieChartSegment(0.02f, 0xFFC0CA33.toInt(), icon),
                PieChartSegment(0.02f, 0xFF3949AB.toInt(), icon),
                PieChartSegment(0.02f, 0xFF00ACC1.toInt(), icon),
                PieChartSegment(0.01f, 0xFFFFB300.toInt(), icon),
                //
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
                PieChartSegment(0.001f, 0xFF00897B.toInt(), icon),
            )
            PieChart(
                segments = segments,
                strokeWidth = 36.dp,
            )
        }
    }
}
