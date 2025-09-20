@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.tag.internal.DefaultCreateTagUseCase
import dev.aleksrychkov.scrooge.feature.tag.internal.DefaultDeleteTagUseCase
import dev.aleksrychkov.scrooge.feature.tag.internal.DefaultObserveTagsUseCase
import kotlinx.coroutines.Dispatchers

fun buildTagModule(): NaiveModule {
    return module {
        factory<CreateTagUseCase> {
            DefaultCreateTagUseCase(
                tagDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<ObserveTagsUseCase> {
            DefaultObserveTagsUseCase(
                tagDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<DeleteTagUseCase> {
            DefaultDeleteTagUseCase(
                tagDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
