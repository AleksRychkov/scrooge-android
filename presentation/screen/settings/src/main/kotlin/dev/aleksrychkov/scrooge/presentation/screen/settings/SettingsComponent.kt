package dev.aleksrychkov.scrooge.presentation.screen.settings

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.settings.internal.DefaultSettingsComponent

interface SettingsComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): SettingsComponent = DefaultSettingsComponent(
            componentContext = componentContext,
        )
    }
}
