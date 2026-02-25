package dev.aleksrychkov.scrooge.presentation.component.limits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.LimitsComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitProgress
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsState
import kotlinx.collections.immutable.toImmutableList
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
        ) {
            Text(
                text = limitProgress.period,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = limitProgress.totalInfo,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(Tinny),
        ) {
            LimitsProgressIndicator(
                weight = limitProgress.progress
            )

            Box(
                modifier = Modifier
                    .height(Large)
                    .width(Tinny)
                    .background(color = MaterialTheme.colorScheme.onBackground, shape = CircleShape)
            )

            if (limitProgress.overflowProgress > 0f) {
                LimitsProgressIndicator(
                    weight = limitProgress.overflowProgress,
                    color = MaterialTheme.colorScheme.error,
                )
            } else if (limitProgress.progress < 1f) {
                LimitsProgressIndicator(
                    weight = 1f - limitProgress.progress,
                    progress = 0.0f,
                )
            }
        }
    }
}

@Composable
private fun RowScope.LimitsProgressIndicator(
    weight: Float,
    color: Color = ProgressIndicatorDefaults.linearColor,
    progress: Float = 1.0f,
) {
    LinearProgressIndicator(
        modifier = Modifier
            .height(Medium)
            .weight(weight = weight),
        progress = { progress },
        gapSize = HalfNormal,
        drawStopIndicator = { null },
        color = color,
    )
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun LimitsContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val progress = LimitProgress(
                period = "\uD83D\uDCC5 " + LimitEntity.Period.Daily.name,
                progress = 1.0f,
                overflowProgress = 0.25f,
                totalInfo = "250₽ over",
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
