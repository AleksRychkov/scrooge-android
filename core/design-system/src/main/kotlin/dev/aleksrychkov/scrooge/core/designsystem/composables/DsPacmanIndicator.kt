package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
Copied from https://github.com/MahboubehSeyedpour/jetpack-loading/blob/master/jetpack-loading/src/main/java/com/spr/jetpack_loading/components/indicators/PacmanIndicator.kt
 */
@Suppress("MagicNumber")
@Composable
fun DsPacmanIndicator(
    color: Color = Color.White,
    ballIn: Boolean = false,
    ballDiameter: Float = 40f,
    canvasSize: Dp = 40.dp,
    animationDuration: Int = 500
) {
    val lipStart = 0f
    val lipEnd = 45f

    val initPositionMultiplier = if (ballIn) -1 else 1
    val targetPositionMultiplier = if (ballIn) 1 else -1

    val positionAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = initPositionMultiplier * ballDiameter,
        targetValue = targetPositionMultiplier * ballDiameter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val lipAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = lipStart,
        targetValue = lipEnd,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration / 2, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.size(canvasSize)) {
        drawArc(
            color = color,
            startAngle = lipAnimation,
            sweepAngle = 360 - lipAnimation.times(2),
            topLeft = Offset(0f, 0f),
            size = Size(size.width, size.height),
            useCenter = true
        )
        drawCircle(
            color = color,
            radius = ballDiameter / 2,
            center = Offset(
                x = size.width + positionAnimation,
                y = size.height / 2
            )
        )
    }
}
