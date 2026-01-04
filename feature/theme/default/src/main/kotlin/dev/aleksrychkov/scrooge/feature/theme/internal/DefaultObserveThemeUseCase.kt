package dev.aleksrychkov.scrooge.feature.theme.internal

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.feature.theme.ObserveThemeUseCase
import dev.aleksrychkov.scrooge.feature.theme.internal.data.repository.ThemeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

internal class DefaultObserveThemeUseCase(
    private val repository: ThemeRepository,
) : ObserveThemeUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<ThemeEntity?> {
        return flowOf(repository)
            .flatMapConcat { repository -> repository.observeTheme() }
            .catch { emit(ThemeEntity()) }
    }
}
