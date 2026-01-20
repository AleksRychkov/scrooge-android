package dev.aleksrychkov.scrooge.core.designsystem.locals

import androidx.compose.runtime.compositionLocalOf

val LocalAppTheme = compositionLocalOf { AppThemeState() }

data class AppThemeState(val useDarkTheme: Boolean = false)
