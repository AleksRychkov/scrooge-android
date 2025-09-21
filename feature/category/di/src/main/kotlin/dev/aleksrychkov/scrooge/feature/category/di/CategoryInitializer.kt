package dev.aleksrychkov.scrooge.feature.category.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.category.buildCategoryModule

@Suppress("unused")
internal class CategoryInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildCategoryModule())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
