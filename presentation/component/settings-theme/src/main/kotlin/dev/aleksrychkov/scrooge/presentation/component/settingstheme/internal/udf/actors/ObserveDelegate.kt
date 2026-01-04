package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.theme.ObserveThemeUseCase
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeCommand
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ObserveDelegate(
    private val useCase: Lazy<ObserveThemeUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: SettingsThemeCommand.ObserveTheme): Flow<SettingsThemeEvent> {
        return useCase.value.invoke().map { SettingsThemeEvent.Internal.Result(theme = it) }
    }
}
