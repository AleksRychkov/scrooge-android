package dev.aleksrychkov.scrooge.core.di

import kotlin.reflect.KClass

internal fun indexKey(clazz: KClass<*>, qualifier: String? = null): String =
    clazz.java.name + qualifier?.let { ":$it" }.orEmpty()
