@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.theme

import android.content.Context
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.di.singleton
import dev.aleksrychkov.scrooge.feature.theme.internal.DefaultObserveThemeUseCase
import dev.aleksrychkov.scrooge.feature.theme.internal.DefaultSetThemeUseCase
import dev.aleksrychkov.scrooge.feature.theme.internal.data.repository.ThemeRepository
import dev.aleksrychkov.scrooge.feature.theme.internal.data.source.ThemeSource
import kotlinx.coroutines.Dispatchers

fun buildThemeModule(context: Context): NaiveModule {
    return module {
        factory<ThemeSource> {
            ThemeSource(
                storeName = "favorite_currency",
                context = context,
            )
        }
        singleton<ThemeRepository> {
            ThemeRepository(source = get())
        }
        singleton<ObserveThemeUseCase> {
            DefaultObserveThemeUseCase(
                repository = get(),
            )
        }
        factory<SetThemeUseCase> {
            DefaultSetThemeUseCase(
                ioDispatcher = Dispatchers.IO,
                repository = getLazy()
            )
        }
    }
}
