package dev.aleksrychkov.scrooge.feature.theme.internal

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.theme.ObserveThemeUseCase
import dev.aleksrychkov.scrooge.feature.theme.internal.data.repository.ThemeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

internal class DefaultObserveThemeUseCase(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: Lazy<ThemeRepository>,
) : ObserveThemeUseCase {
    override suspend fun invoke(): Flow<ThemeEntity> = withContext(ioDispatcher) {
        runSuspendCatching {
            repository.value.observeTheme()
        }.getOrDefault(emptyFlow())
    }
}
