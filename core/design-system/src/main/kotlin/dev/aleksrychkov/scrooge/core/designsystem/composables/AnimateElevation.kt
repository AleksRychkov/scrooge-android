package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.animateElevation(durationMillis: Int = 300): State<Dp> {
    return animateDpAsState(
        targetValue = this,
        animationSpec = tween(durationMillis = durationMillis),
    )
}
