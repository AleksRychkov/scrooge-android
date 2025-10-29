package dev.aleksrychkov.scrooge.core.designsystem.composables

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Suppress("MagicNumber", "LongMethod")
@Composable
fun AppTabBar(
    modifier: Modifier,
    options: List<String>,
    selectedIndex: Int = 0,
    onOptionSelected: (Int) -> Unit
) {
    val indicatorOffset = remember { Animatable(0f) }

    val density = LocalDensity.current

    val segmentWidth = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(selectedIndex, segmentWidth) {
        indicatorOffset.animateTo(
            targetValue = selectedIndex * segmentWidth.floatValue,
            animationSpec = tween(250)
        )
    }

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(CardDefaults.shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Small)
    ) {
        BoxWithConstraints {
            val totalWidth = maxWidth
            val segmentCount = options.size
            val segmentPx = with(density) { (totalWidth / segmentCount).toPx() }
            segmentWidth.floatValue = segmentPx

            Box(
                modifier = Modifier
                    .offset { IntOffset(indicatorOffset.value.roundToInt(), 0) }
                    .width(totalWidth / segmentCount)
                    .fillMaxHeight()
                    .shadow(Small, CardDefaults.shape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEachIndexed { index, option ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(CardDefaults.shape)
                            .clickable { onOptionSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        val isSelected = index == selectedIndex
                        val textColor = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            animateColorAsState(
                                targetValue = if (isSelected) Color.White else Color.Black,
                                animationSpec = tween(durationMillis = 250),
                            ).value
                        }
                        Text(
                            text = option,
                            color = textColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
