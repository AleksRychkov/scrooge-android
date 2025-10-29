@file:Suppress("MagicNumber")

package dev.aleksrychkov.scrooge.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// https://dribbble.com/shots/21887035-Mobile-App-Design-for-a-Currency-Converter-App

val primaryBlue = Color(0xFF2196F3)

val onPrimaryWhite = Color(0xFFFFFFFF)

val lightBackground = Color(0xFFF0F2F5)
val lightSurface = Color(0xFFFFFFFF)
val lightOnSurface = Color(0xFF1C1B1F)

internal val LightScheme = lightColorScheme(
    primary = primaryBlue,
    onPrimary = onPrimaryWhite,
    primaryContainer = Color(0xFFD3E0EA),
    onPrimaryContainer = Color(0xFF001F2A),

    secondary = Color(0xFF52616B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E4ED),
    onSecondaryContainer = Color(0xFF0F1B25),

    tertiary = Color(0xFF5C527A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE4DAFF),
    onTertiaryContainer = Color(0xFF190F34),

    background = lightBackground,
    onBackground = lightOnSurface,

    surface = lightSurface,
    surfaceContainerLow = lightSurface,
    onSurface = lightOnSurface,

    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF49454E),

    outline = Color(0xFF7A757F),
    error = Color(0xFFDD553A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

val darkBackground = Color(0xFF121212)
val darkSurface = Color(0xFF1E1E1E)
val darkOnSurface = Color(0xFFE0E0E0)

internal val DarkScheme = darkColorScheme(
    primary = primaryBlue,
    onPrimary = onPrimaryWhite,
    primaryContainer = Color(0xFF004B67),
    onPrimaryContainer = Color(0xFFC0E8FF),

    secondary = Color(0xFFBCC7D0),
    onSecondary = Color(0xFF24313B),
    secondaryContainer = Color(0xFF3B4852),
    onSecondaryContainer = Color(0xFFD6E4ED),

    tertiary = Color(0xFFC6BEF7),
    onTertiary = Color(0xFF2E2449),
    tertiaryContainer = Color(0xFF453B61),
    onTertiaryContainer = Color(0xFFE4DAFF),

    background = darkBackground,
    onBackground = darkOnSurface,

    surface = darkSurface,
    surfaceContainerLow = Color.Black,
    onSurface = darkOnSurface,

    surfaceVariant = Color(0xFF1D1D16),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF948F99),
    error = Color(0xFFE58776),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

val ColorCurrency = Color(0xFF64AF64)
