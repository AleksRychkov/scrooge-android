package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity

@Immutable
internal data class SettingsThemeState(
    val theme: ThemeEntity = ThemeEntity(),
)
