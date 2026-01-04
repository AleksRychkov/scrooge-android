package dev.aleksrychkov.scrooge.feature.theme.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.feature.theme.internal.data.source.ThemeSource
import kotlinx.coroutines.flow.Flow

internal interface ThemeRepository {
    companion object {
        operator fun invoke(
            source: ThemeSource
        ): ThemeRepository = DefaultThemeRepository(source = source)
    }

    suspend fun observeTheme(): Flow<ThemeEntity?>
    suspend fun setTheme(theme: ThemeEntity)
}

private class DefaultThemeRepository(
    private val source: ThemeSource,
) : ThemeRepository {
    override suspend fun observeTheme(): Flow<ThemeEntity?> =
        source.observe()

    override suspend fun setTheme(theme: ThemeEntity) {
        source.set(theme)
    }
}
