package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.designsystem.composables.noRippleClickable

@Composable
internal fun IntermediateContent(modifier: Modifier) {
    Box(modifier = modifier.noRippleClickable {})
}
