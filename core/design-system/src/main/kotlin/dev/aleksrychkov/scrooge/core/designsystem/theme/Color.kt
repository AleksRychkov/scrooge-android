@file:Suppress("MagicNumber")

package dev.aleksrychkov.scrooge.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val primaryBlue = Color(0xFF2196F3)

internal val LightScheme = lightColorScheme(
    primary = primaryBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF7F7F7),
    onPrimaryContainer = Color(0xFF363636),

    secondary = Color(0xFFF7F7F7),
    onSecondary = Color(0xFF363636),
    secondaryContainer = Color(0xFFD6E4ED),
    onSecondaryContainer = Color(0xFF0F1B25),

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF363636),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF363636),
    surfaceContainerHighest = Color(0xFFFFFFFF), // Card container color
    surfaceContainerLow = Color(0xFFFFFFFF), // Card container color

    outline = Color(0xFF363636),
    error = Color(0xFFDD553A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

internal val DarkScheme = darkColorScheme(
    primary = primaryBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF222223),
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFF222223),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF3B4852),
    onSecondaryContainer = Color(0xFFD6E4ED),

    background = Color(0xFF191919),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF191919),
    onSurface = Color(0xFFFFFFFF),
    surfaceContainerHighest = Color(0xFF191919), // Card container color
    surfaceContainerLow = Color(0xFF191919), // Card container color

    surfaceVariant = Color(0xFF1D1D16),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF948F99),
    error = Color(0xFFE58776),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

val IncomeColor = Color(0xFF68B73F)
val ExpenseColor = Color(0xFFCF6F6B)
