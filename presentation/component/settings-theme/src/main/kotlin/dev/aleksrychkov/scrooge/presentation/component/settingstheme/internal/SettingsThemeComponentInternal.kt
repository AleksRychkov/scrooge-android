package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeState
import kotlinx.coroutines.flow.StateFlow

internal interface SettingsThemeComponentInternal : SettingsThemeComponent {
    val state: StateFlow<SettingsThemeState>

    fun setThemeType(type: ThemeEntity.Type)
}
