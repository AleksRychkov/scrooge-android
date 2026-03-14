package dev.aleksrychkov.scrooge.core.designsystem.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal

internal val MAX_INFIX_TEXT_SIZE = 60.sp
internal val MIN_INFIX_TEXT_SIZE = 30.sp
private const val AUTO_FONT_SIZE_ANIMATION_DURATION = 250
private const val MIDDLE_PRECISION = 0.5f

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DsAutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign? = null,
    style: TextStyle? = null,
    color: Color = Color.Unspecified,
    maxFontSize: TextUnit = MAX_INFIX_TEXT_SIZE,
    minFontSize: TextUnit = MIN_INFIX_TEXT_SIZE,
) {
    var fontSize by remember { mutableStateOf(maxFontSize) }
    val density = LocalDensity.current

    val txtMeasurer = rememberTextMeasurer()
    BoxWithConstraints(
        modifier = modifier.wrapContentHeight(),
        contentAlignment = Alignment.Center,
    ) {
        val maxWidthPx = with(density) { (maxWidth - Normal).toPx() }
        LaunchedEffect(text) {
            var low = minFontSize.value
            var high = maxFontSize.value

            while (high - low > MIDDLE_PRECISION) {
                val mid = (low + high) / 2f
                val width = txtMeasurer.measure(
                    text,
                    maxLines = 1,
                    style = (style ?: TextStyle.Default).copy(fontSize = mid.sp),
                )
                if (width.size.width <= maxWidthPx) {
                    low = mid
                } else {
                    high = mid
                }
            }

            fontSize = low.sp
        }

        val animatedFontSize by animateFloatAsState(
            targetValue = fontSize.value,
            animationSpec = tween(durationMillis = AUTO_FONT_SIZE_ANIMATION_DURATION),
            label = "autoSizeAnimation"
        )

        Text(
            text = text,
            textAlign = textAlign,
            color = color,
            style = style ?: LocalTextStyle.current,
            maxLines = 1,
            fontSize = animatedFontSize.sp,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun AutoSizeTextPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DsAutoSizeText(
                modifier = Modifier.padding(Large),
                text = "12312312",
                minFontSize = 2.sp,
            )
        }
    }
}
