package dev.aleksrychkov.scrooge.feature.theme.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.feature.theme.internal.data.source.ThemeSource
import kotlinx.coroutines.flow.Flow

internal interface ThemeRepository {
    companion object {
        operator fun invoke(
            source: Lazy<ThemeSource>
        ): ThemeRepository = DefaultThemeRepository(source = source)
    }

    suspend fun observeTheme(): Flow<ThemeEntity>
    suspend fun setTheme(theme: ThemeEntity)
}

private class DefaultThemeRepository(
    private val source: Lazy<ThemeSource>,
) : ThemeRepository {
    override suspend fun observeTheme(): Flow<ThemeEntity> =
        source.value.observe()

    override suspend fun setTheme(theme: ThemeEntity) {
        source.value.set(theme)
    }
}
