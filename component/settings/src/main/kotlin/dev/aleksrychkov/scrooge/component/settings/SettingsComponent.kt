package dev.aleksrychkov.scrooge.component.settings

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.settings.internal.DefaultSettingsComponent

interface SettingsComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): SettingsComponent = DefaultSettingsComponent(
            componentContext = componentContext,
        )
    }
}
