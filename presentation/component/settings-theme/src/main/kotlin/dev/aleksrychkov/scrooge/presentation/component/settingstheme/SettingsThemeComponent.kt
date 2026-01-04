package dev.aleksrychkov.scrooge.presentation.component.settingstheme

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.DefaultSettingsThemeComponent

interface SettingsThemeComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): SettingsThemeComponent {
            return DefaultSettingsThemeComponent(componentContext = componentContext)
        }
    }
}
