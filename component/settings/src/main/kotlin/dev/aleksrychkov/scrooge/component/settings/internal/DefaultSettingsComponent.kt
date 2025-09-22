package dev.aleksrychkov.scrooge.component.settings.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultSettingsComponent(
    private val componentContext: ComponentContext
) : SettingsComponentInternal, ComponentContext by componentContext
