package dev.aleksrychkov.scrooge.feature.tag.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.tag.buildTagModule

@Suppress("unused")
internal class TagInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildTagModule())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
