package dev.aleksrychkov.scrooge.feature.limits.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.limits.buildLimitsModule

internal class LimitsInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildLimitsModule())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
