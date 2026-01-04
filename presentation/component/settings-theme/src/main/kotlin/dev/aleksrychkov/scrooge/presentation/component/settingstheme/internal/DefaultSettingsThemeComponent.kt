package dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeActor
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeEvent
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeReducer
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultSettingsThemeComponent(
    componentContext: ComponentContext
) : SettingsThemeComponentInternal, ComponentContext by componentContext {

    private val store: Store<SettingsThemeState, SettingsThemeEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = SettingsThemeState(),
            actor = SettingsThemeActor(),
            reducer = SettingsThemeReducer(),
            startEvent = SettingsThemeEvent.External.Init,
        )
    }

    override val state: StateFlow<SettingsThemeState>
        get() = store.state

    override fun setThemeType(type: ThemeEntity.Type) {
        store.handle(SettingsThemeEvent.External.SetTheme(themeType = type))
    }
}
