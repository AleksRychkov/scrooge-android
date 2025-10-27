@file:Suppress("LongParameterList", "LongMethod", "MagicNumber")

package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Displays a Snackbar with a countdown timer
 *
 * @param snackbarData The data for this snackbar showing via [SnackbarHostState]
 * @param modifier The [Modifier] to be applied to this snackbar
 * @param durationInSeconds Duration of the countdown timer
 * @param actionOnNewLine Whether to display the action on a separate line
 * @param shape The shape of this snackbar's container
 * @param containerColor The color used for the background of this snackbar
 * @param contentColor The preferred color for content inside this snackbar
 * @param actionColor The color of the snackbar's action
 * @param actionContentColor The preferred content color for the optional action inside this snackbar.
 * @param dismissActionContentColor The preferred content color for the optional dismiss action inside this snackbar.
 */
@Composable
fun CountdownSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    durationInSeconds: Int = 5,
    actionOnNewLine: Boolean = false,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionColor: Color = SnackbarDefaults.actionColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {
    if ((snackbarData.visuals as? CountdownSnackbarVisualsImpl)?.useCountDown != true) {
        // fallback
        Snackbar(snackbarData = snackbarData)
        return
    }

    val totalDuration = remember(durationInSeconds) { durationInSeconds * 1000 }
    var millisRemaining by remember { mutableIntStateOf(totalDuration) }

    LaunchedEffect(snackbarData) {
        while (millisRemaining > 0) {
            delay(40)
            millisRemaining -= 40
        }
        snackbarData.dismiss()
    }

    val actionLabel = snackbarData.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
                onClick = { snackbarData.performAction() },
                content = { Text(actionLabel) }
            )
        }
    } else {
        null
    }

    val dismissActionComposable: (@Composable () -> Unit)? =
        if (snackbarData.visuals.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = { snackbarData.dismiss() },
                    content = {
                        Icon(Icons.Rounded.Close, null)
                    }
                )
            }
        } else {
            null
        }

    Snackbar(
        modifier = modifier.padding(12.dp),
        action = actionComposable,
        actionOnNewLine = actionOnNewLine,
        dismissAction = dismissActionComposable,
        dismissActionContentColor = dismissActionContentColor,
        actionContentColor = actionContentColor,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SnackbarCountdown(
                timerProgress = millisRemaining.toFloat() / totalDuration.toFloat(),
                secondsRemaining = (millisRemaining / 1000) + 1,
                color = contentColor
            )
            Text(snackbarData.visuals.message)
        }
    }
}

@Composable
private fun SnackbarCountdown(
    timerProgress: Float,
    secondsRemaining: Int,
    color: Color
) {
    Box(
        modifier = Modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.matchParentSize()) {
            val strokeStyle = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = color.copy(alpha = 0.12f),
                style = strokeStyle
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = (-360f * timerProgress),
                useCenter = false,
                style = strokeStyle
            )
        }
        Text(
            text = secondsRemaining.toString(),
            style = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color = color
            )
        )
    }
}

@Stable
class CountdownSnackbarVisualsImpl(
    override val message: String,
    override val actionLabel: String?,
    override val withDismissAction: Boolean,
    override val duration: SnackbarDuration,
    val useCountDown: Boolean,
) : SnackbarVisuals {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CountdownSnackbarVisualsImpl

        if (message != other.message) return false
        if (actionLabel != other.actionLabel) return false
        if (withDismissAction != other.withDismissAction) return false
        if (duration != other.duration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + actionLabel.hashCode()
        result = 31 * result + withDismissAction.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}

suspend fun SnackbarHostState.showCountdownSnackbar(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration =
        if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    useCountDown: Boolean = false,
): SnackbarResult {
    return showSnackbar(
        CountdownSnackbarVisualsImpl(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = if (useCountDown) SnackbarDuration.Indefinite else duration,
            useCountDown = useCountDown,
        )
    )
}
