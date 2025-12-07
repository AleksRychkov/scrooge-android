package dev.aleksrychkov.scrooge.feature.reports.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.reports.buildReportsModule

internal class ReportsInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildReportsModule())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
