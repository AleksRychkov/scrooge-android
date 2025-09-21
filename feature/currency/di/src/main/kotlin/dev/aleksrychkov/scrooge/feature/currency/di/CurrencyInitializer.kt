package dev.aleksrychkov.scrooge.feature.currency.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.currency.buildCurrencyModule

@Suppress("unused")
internal class CurrencyInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildCurrencyModule())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
