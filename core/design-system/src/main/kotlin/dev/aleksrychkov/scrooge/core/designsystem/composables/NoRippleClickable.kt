package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.noRippleClickable(
    onClick: () -> Unit
) = this.clickable(
    interactionSource = null,
    indication = null,
    onClick = onClick,
)
