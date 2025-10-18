package dev.aleksrychkov.scrooge.core.resources.internal.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.di.singleton
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.resources.internal.DefaultResourceManager

internal class ResourceManagerInitializer : Initializer<ResourceManager> {
    override fun create(context: Context): ResourceManager {
        val resourceManager: ResourceManager = DefaultResourceManager(context = context)
        val module = module {
            singleton<ResourceManager> { resourceManager }
        }
        Naive.add(module)
        return resourceManager
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
