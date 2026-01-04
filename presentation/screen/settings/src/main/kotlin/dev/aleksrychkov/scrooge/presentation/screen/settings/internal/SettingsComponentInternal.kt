package dev.aleksrychkov.scrooge.presentation.screen.settings.internal

import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.screen.settings.SettingsComponent

internal interface SettingsComponentInternal : SettingsComponent {
    val settingsThemeComponent: SettingsThemeComponent
}
