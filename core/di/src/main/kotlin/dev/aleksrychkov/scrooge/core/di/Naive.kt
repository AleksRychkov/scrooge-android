package dev.aleksrychkov.scrooge.core.di

import kotlin.reflect.KClass

object Naive {

    private val lock = Any()
    private val instances = hashMapOf<String, NaiveProvider<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: KClass<*>, qualifier: String? = null): T {
        val key = indexKey(clazz, qualifier)
        return synchronized(lock) {
            @Suppress("UNCHECKED_CAST")
            instances[key]?.get() as? T ?: error("Naive:: Instance $key not found")
        }
    }

    fun add(module: NaiveModule) {
        synchronized(lock) {
            module.keyValues().forEach { (indexKey, factory) ->
                instances[indexKey] = factory
            }
        }
    }

    fun add(vararg modules: NaiveModule) {
        synchronized(lock) {
            modules.forEach { module ->
                module.keyValues().forEach { (indexKey, factory) ->
                    instances[indexKey] = factory
                }
            }
        }
    }

    fun remove(module: NaiveModule) {
        synchronized(lock) {
            module.keys().forEach { indexKey ->
                instances.remove(indexKey)
            }
        }
    }

    fun remove(vararg modules: NaiveModule) {
        synchronized(lock) {
            modules.forEach { module ->
                module.keys().forEach { indexKey ->
                    instances.remove(indexKey)
                }
            }
        }
    }
}

inline fun <reified T> getLazy(qualifier: String? = null): Lazy<T> = lazy {
    Naive.get(T::class, qualifier)
}

inline fun <reified T : Any> get(qualifier: String? = null): T =
    Naive.get(T::class, qualifier)
