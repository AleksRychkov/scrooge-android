package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.actors.ObserveDelegate
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.actors.SetThemeDelegate
import kotlinx.coroutines.flow.Flow

internal class SettingsThemeActor(
    private val observeDelegate: ObserveDelegate = ObserveDelegate(),
    private val setDelegate: SetThemeDelegate = SetThemeDelegate(),
) : Actor<SettingsThemeCommand, SettingsThemeEvent> {
    override suspend fun process(command: SettingsThemeCommand): Flow<SettingsThemeEvent> {
        return when (command) {
            is SettingsThemeCommand.ObserveTheme -> observeDelegate.invoke(command)
            is SettingsThemeCommand.SetTheme -> setDelegate.invoke(command)
        }
    }
}
