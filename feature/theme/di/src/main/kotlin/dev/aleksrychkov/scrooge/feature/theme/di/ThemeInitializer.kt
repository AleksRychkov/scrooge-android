package dev.aleksrychkov.scrooge.feature.theme.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.theme.buildThemeModule

internal class ThemeInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildThemeModule(context = context))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
