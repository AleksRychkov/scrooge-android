package dev.aleksrychkov.scrooge.presentation.component.limits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.LimitsComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitProgress
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsState
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun LimitsContent(
    modifier: Modifier,
    component: LimitsComponent,
) {
    LimitsContent(
        modifier = modifier,
        component = component as LimitsComponentInternal,
    )
}

@Composable
private fun LimitsContent(
    modifier: Modifier,
    component: LimitsComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    if (state.isVisible && state.limits.isNotEmpty()) {
        LimitsContent(
            modifier = modifier,
            state = state,
        )
    }
}

@Composable
private fun LimitsContent(
    modifier: Modifier,
    state: LimitsState,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(Resources.string.limits),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        )

        state.limits.forEach { progress ->
            LimitProgress(
                modifier = Modifier.fillMaxWidth(),
                limitProgress = progress,
            )
        }
    }
}

@Composable
private fun LimitProgress(
    modifier: Modifier,
    limitProgress: LimitProgress,
) {
    Column(
        modifier = modifier
            .padding(top = Normal)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = limitProgress.period,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )

            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.labelLarge.fontSize)) {
                    append(limitProgress.spent)
                }
                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.labelSmall.fontSize)) {
                    append(" / ")
                    append(limitProgress.limit)
                    append(" ")
                    append(limitProgress.currencySymbol)
                }
            }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        LimitProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Normal),
            limitProgress = limitProgress.progress,
            overflowProgress = limitProgress.overflowProgress,
        )
    }
}

@Composable
private fun LimitProgressBar(
    modifier: Modifier,
    limitProgress: Float,
    overflowProgress: Float,
) {
    val density = LocalDensity.current
    val minBarWidth = Normal
    val minBarHeight = 48.dp
    BoxWithConstraints(
        modifier = modifier
    ) {
        val barsCount = with(density) {
            maxWidth.toPx() / (minBarWidth + Medium).toPx()
        }.roundToInt()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(minBarHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(barsCount) { index ->
                val color = if (index / barsCount.toFloat() <= limitProgress) {
                    MaterialTheme.colorScheme.primary
                } else {
                    if (overflowProgress > 0) {
                        ExpenseColor
                    } else {
                        ProgressIndicatorDefaults.linearTrackColor
                    }
                }
                Box(
                    modifier = Modifier
                        .width(minBarWidth)
                        .height(minBarHeight)
                        .dropShadow(
                            shape = CardDefaults.shape,
                            shadow = Shadow(
                                radius = 5.dp,
                                spread = 1.dp,
                                color = color.copy(alpha = 0.5f),
                                offset = DpOffset(x = 0.dp, 0.dp)
                            )
                        )
                        .background(
                            color = color,
                            shape = CardDefaults.shape,
                        )
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun LimitsContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val progress = LimitProgress(
                period = "\uD83D\uDCC5 " + LimitEntity.Period.Daily.name,
                progress = 0.75f,
                overflowProgress = 0.25f,
                limit = "1 000",
                spent = "250",
                currencySymbol = "₽",
            )
            LimitsContent(
                modifier = Modifier.fillMaxWidth(),
                state = LimitsState(
                    isVisible = true,
                    limits = listOf(progress).toImmutableList()
                ),
            )
        }
    }
}
