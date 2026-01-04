package dev.aleksrychkov.scrooge.feature.theme

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import kotlinx.coroutines.flow.Flow

fun interface ObserveThemeUseCase {
    operator fun invoke(): Flow<ThemeEntity?>
}
