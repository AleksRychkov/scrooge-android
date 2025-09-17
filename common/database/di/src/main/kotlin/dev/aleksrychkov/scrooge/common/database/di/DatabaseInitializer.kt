package dev.aleksrychkov.scrooge.common.database.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.common.database.buildDatabaseModule
import dev.aleksrychkov.scrooge.core.di.Naive

@Suppress("unused")
internal class DatabaseInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildDatabaseModule(context))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
