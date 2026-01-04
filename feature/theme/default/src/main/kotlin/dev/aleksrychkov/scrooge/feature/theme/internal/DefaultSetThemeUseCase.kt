package dev.aleksrychkov.scrooge.feature.theme.internal

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.theme.SetThemeUseCase
import dev.aleksrychkov.scrooge.feature.theme.internal.data.repository.ThemeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultSetThemeUseCase(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: Lazy<ThemeRepository>,
) : SetThemeUseCase {
    override suspend fun invoke(theme: ThemeEntity): Unit = withContext(ioDispatcher) {
        runSuspendCatching {
            repository.value.setTheme(theme)
        }
    }
}
