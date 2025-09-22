package dev.aleksrychkov.scrooge.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content:
    @Composable()
    () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightScheme
    } else {
        DarkScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) { content() }
    }
}
