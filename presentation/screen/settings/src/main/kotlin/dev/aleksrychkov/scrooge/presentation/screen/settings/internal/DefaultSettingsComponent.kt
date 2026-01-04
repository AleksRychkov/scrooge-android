package dev.aleksrychkov.scrooge.presentation.screen.settings.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent

internal class DefaultSettingsComponent(
    private val componentContext: ComponentContext
) : SettingsComponentInternal, ComponentContext by componentContext {

    private val _settingsThemeComponent: SettingsThemeComponent by lazy {
        SettingsThemeComponent(
            componentContext = childContext("SettingsComponentThemeComponent")
        )
    }

    override val settingsThemeComponent: SettingsThemeComponent
        get() = _settingsThemeComponent
}
