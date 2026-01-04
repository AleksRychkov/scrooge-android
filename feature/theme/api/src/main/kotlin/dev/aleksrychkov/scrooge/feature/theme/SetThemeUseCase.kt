package dev.aleksrychkov.scrooge.feature.theme

import dev.aleksrychkov.scrooge.core.entity.ThemeEntity

fun interface SetThemeUseCase {
    suspend operator fun invoke(theme: ThemeEntity)
}
