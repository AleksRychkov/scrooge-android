package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class SettingsThemeReducer :
    Reducer<SettingsThemeState, SettingsThemeEvent, SettingsThemeCommand, Unit> {
    override fun reduce(
        event: SettingsThemeEvent,
        state: SettingsThemeState
    ): ReducerResult<SettingsThemeState, SettingsThemeCommand, Unit> {
        return when (event) {
            SettingsThemeEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(SettingsThemeCommand.ObserveTheme)
                }
            }

            is SettingsThemeEvent.Internal.Result -> state.reduceWith(event) {
                state {
                    copy(theme = event.theme)
                }
            }

            is SettingsThemeEvent.External.SetTheme -> state.reduceWith(event) {
                if (state.theme.type != event.themeType) {
                    command {
                        listOf(SettingsThemeCommand.SetTheme(theme = ThemeEntity(event.themeType)))
                    }
                }
            }
        }
    }
}
