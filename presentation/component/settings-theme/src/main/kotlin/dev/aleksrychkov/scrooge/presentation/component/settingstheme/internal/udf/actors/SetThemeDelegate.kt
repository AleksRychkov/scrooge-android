package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.theme.SetThemeUseCase
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeCommand
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class SetThemeDelegate(
    private val useCase: Lazy<SetThemeUseCase> = getLazy()
) {
    suspend operator fun invoke(cmd: SettingsThemeCommand.SetTheme): Flow<SettingsThemeEvent> {
        useCase.value.invoke(theme = cmd.theme)
        return emptyFlow()
    }
}
