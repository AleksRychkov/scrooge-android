@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.limits.internal.DefaultLimitsCreateUseCase
import dev.aleksrychkov.scrooge.feature.limits.internal.DefaultLimitsDeleteUseCase
import dev.aleksrychkov.scrooge.feature.limits.internal.DefaultLimitsObserveTotalUseCase
import dev.aleksrychkov.scrooge.feature.limits.internal.DefaultLimitsObserveUseCase
import dev.aleksrychkov.scrooge.feature.limits.internal.DefaultLimitsUpdateUseCase
import kotlinx.coroutines.Dispatchers

fun buildLimitsModule(): NaiveModule {
    return module {
        factory<LimitsCreateUseCase> {
            DefaultLimitsCreateUseCase(
                dao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<LimitsUpdateUseCase> {
            DefaultLimitsUpdateUseCase(
                dao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<LimitsDeleteUseCase> {
            DefaultLimitsDeleteUseCase(
                dao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<LimitsGetUseCase> {
            DefaultLimitsObserveUseCase(
                dao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<LimitsObserveTotalUseCase> {
            DefaultLimitsObserveTotalUseCase(
                dao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
