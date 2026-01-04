package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity

internal sealed interface SettingsThemeEvent {
    sealed interface External : SettingsThemeEvent {
        data object Init : External
        data class SetTheme(val themeType: ThemeEntity.Type) : External
    }

    sealed interface Internal : SettingsThemeEvent {
        data class Result(val theme: ThemeEntity?) : Internal
    }
}
