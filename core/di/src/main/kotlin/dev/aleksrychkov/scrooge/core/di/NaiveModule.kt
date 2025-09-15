package dev.aleksrychkov.scrooge.core.di

import kotlin.reflect.KClass

class NaiveModule {

    private val intermediates = hashMapOf<String, NaiveProvider<*>>()

    fun <T> factory(
        clazz: KClass<*>,
        qualifier: String? = null,
        definition: NaiveDefinition<T>
    ) {
        intermediates[indexKey(clazz, qualifier)] = NaiveFactoryProvider(definition)
    }

    fun <T> singleton(
        clazz: KClass<*>,
        qualifier: String? = null,
        definition: NaiveDefinition<T>
    ) {
        intermediates[indexKey(clazz, qualifier)] = NaiveSingletonProvider(definition)
    }

    internal fun keys(): Set<String> = intermediates.keys

    internal fun keyValues(): List<Pair<String, NaiveProvider<*>>> = intermediates.toList()
}

inline fun <reified T> NaiveModule.factory(
    qualifier: String? = null,
    noinline definition: NaiveDefinition<T>,
) {
    factory(T::class, qualifier, definition)
}

inline fun <reified T> NaiveModule.singleton(
    qualifier: String? = null,
    noinline definition: NaiveDefinition<T>,
) {
    singleton(T::class, qualifier, definition)
}

inline fun module(block: NaiveModule.() -> Unit): NaiveModule =
    NaiveModule().apply(block)
