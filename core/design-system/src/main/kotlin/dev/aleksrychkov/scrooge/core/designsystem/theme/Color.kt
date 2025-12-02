@file:Suppress("MagicNumber")

package dev.aleksrychkov.scrooge.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// https://dribbble.com/shots/21887035-Mobile-App-Design-for-a-Currency-Converter-App

val primaryBlue = Color(0xFF2196F3)

val onPrimaryWhite = Color(0xFFFFFFFF)

val lightBackground = Color(0xFFFFFFFF)
val lightOnSurface = Color(0xFF363636)

internal val LightScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF7F7F7),
    onPrimaryContainer = Color(0xFF363636),

    secondary = Color(0xFFF7F7F7),
    onSecondary = Color(0xFF363636),
    secondaryContainer = Color(0xFFD6E4ED),
    onSecondaryContainer = Color(0xFF0F1B25),

    tertiary = Color(0xFF5C527A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE4DAFF),
    onTertiaryContainer = Color(0xFF190F34),

    background = lightBackground,
    onBackground = lightOnSurface,

    surface = lightBackground,
    onSurface = lightOnSurface,
    surfaceContainerHighest = Color(0xFFFFFFFF), // Card container color
    surfaceContainerLow = Color(0xFFFFFFFF), // Card container color

    outline = Color(0xFF363636),
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
    surfaceContainerLow = darkSurface,
    onSurface = darkOnSurface,

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
