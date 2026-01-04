package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity

internal sealed interface SettingsThemeCommand {
    data object ObserveTheme : SettingsThemeCommand
    data class SetTheme(val theme: ThemeEntity) : SettingsThemeCommand
}
