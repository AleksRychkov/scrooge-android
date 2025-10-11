package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationBarSpacer() {
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
}
