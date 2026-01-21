@file:Suppress("All")

package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun <T> DsWheel(
    modifier: Modifier = Modifier,
    data: List<T> = emptyList(),
    rowCount: Int = 5,
    selectedItem: T,
    verticalPadding: Dp = Medium,
    labelStyle: TextStyle = MaterialTheme.typography.titleLarge,
    labelColor: Color = Color.Unspecified,
    labelAsString: @Composable T.() -> String = { this.toString() },
    onItemSelected: (T) -> Unit = {},
) {
    require(rowCount > 1 && rowCount % 2 != 0) {
        "\"rowCount\" must be greater than 1 and odd"
    }

    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val itemHeight = remember(labelStyle) {
        val result = textMeasurer.measure(
            text = AnnotatedString("Sample"),
            style = TextStyle(fontSize = labelStyle.fontSize)
        )
        with(density) {
            result.size.height.toDp() + verticalPadding + verticalPadding
        }
    }
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val layoutHeightPx = itemHeightPx * rowCount
    val radiusPx = layoutHeightPx / 2
    val halfCircumference = layoutHeightPx * PI   // full semicircle for rotation
    val centerPx = layoutHeightPx / 2 - itemHeightPx / 2
    val virtualHeightPx = (data.size - 1) * itemHeightPx


    val selectedIndex = data.indexOf(selectedItem)
    val coroutineScope = rememberCoroutineScope()
    val animatedOffset = remember { Animatable(0f) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = selectedItem, key2 = data) {
        if (!animatedOffset.isRunning && !isDragging) {
            animatedOffset.snapTo(-selectedIndex * itemHeightPx)
        }
    }

    Box(
        modifier = modifier
            .height(with(density) { layoutHeightPx.toDp() })
            .clipToBounds()
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    val oldOffset = animatedOffset.value
                    var offset = oldOffset
                    offset -= -deltaY // invert deltaY
                    // Clamp to bounds
                    if (offset < itemHeightPx / 2f && abs(offset) <= virtualHeightPx + itemHeightPx / 2f) {
                        // OK
                    } else {
                        offset = oldOffset
                    }
                    coroutineScope.launch {
                        animatedOffset.snapTo(offset)

                        val currentIndex = abs(
                            ((offset / itemHeightPx) % (data.size)).roundToInt()
                        ).coerceIn(0, data.size - 1)
                        if (selectedIndex != currentIndex) {
                            val middle = layoutHeightPx / 2 - itemHeightPx / 2
                            val t = abs((currentIndex * itemHeightPx + offset) / middle)
                            if (t < 0.1f) {
                                onItemSelected(data[currentIndex])
                            }
                        }
                    }
                },
                onDragStarted = {
                    isDragging = true
                },
                onDragStopped = { velocity ->
                    coroutineScope.launch {
                        val offset = animatedOffset.fling(
                            initialVelocity = velocity / 3f,
                            lowerBound = itemHeightPx,
                            upperBound = virtualHeightPx - itemHeightPx,
                            itemHeight = itemHeightPx,
                        ).endState.value

                        isDragging = false
                        val currentIndex =
                            abs(((offset / itemHeightPx) % (data.size)).roundToInt())
                        if (selectedIndex != currentIndex) {
                            onItemSelected(data[currentIndex])
                        }
                    }
                }
            )
    ) {
        val offset = animatedOffset.value
        val currentIndex = (-offset / itemHeightPx).roundToInt()

        val from = (currentIndex - rowCount / 2 - 1).coerceAtLeast(0)
        val to = (currentIndex + rowCount / 2 + 1).coerceAtMost(data.lastIndex)

        for (i in from..to) {
            val distanceFromCenter = i * itemHeightPx + offset
            val t = distanceFromCenter / centerPx

            // Stronger curvature
            val radian = (distanceFromCenter * PI / halfCircumference) * 2
            val translateY = centerPx + radiusPx * sin(radian).toFloat()

            // Rotation along X-axis (edges rotate more naturally)
            val rotationX = (-radian * 180 / PI).toFloat().coerceIn(-90f, 90f)

            // Minimal scaling for perspective (optional)
            val scale = 1f.coerceAtLeast(1f - abs(t) * 0.05f)

            // Optional alpha fading
            val alpha = (1f - abs(t) * 0.6f).coerceIn(0.2f, 1f)

            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
                    .offset { IntOffset(0, translateY.roundToInt()) }
                    .graphicsLayer(
                        rotationX = rotationX,
                        scaleX = scale,
                        scaleY = scale,
                        alpha = alpha,
                        transformOrigin = TransformOrigin.Center
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labelAsString(data[i]),
                    style = labelStyle,
                    color = labelColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    lowerBound: Float = 0f,
    upperBound: Float,
    itemHeight: Float
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = exponentialDecay<Float>(frictionMultiplier = 1f).calculateTargetValue(
        value,
        initialVelocity
    )
    val target = when {
        targetValue > lowerBound -> itemHeight / 2f
        abs(targetValue) >= upperBound + itemHeight -> -(upperBound)
        else -> targetValue
    }
    val coercedTarget = target % itemHeight
    val coercedAnchors = listOf(-itemHeight, 0f, itemHeight)
    val coercedPoint = coercedAnchors.minBy { abs(it - coercedTarget) }
    val base = itemHeight * (target / itemHeight).toInt()
    val adjustTarget = coercedPoint + base
    return animateTo(
        targetValue = adjustTarget,
        animationSpec = spring(dampingRatio = 1f, stiffness = Spring.StiffnessLow),
        initialVelocity = initialVelocity
    )
}

@Preview
@Composable
private fun DsWheelPreview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            DsWheel(
                data = (0..50).toList(),
                selectedItem = 10,
                rowCount = 5,
                onItemSelected = { println("Selected: $it") },
            )
        }
    }
}
