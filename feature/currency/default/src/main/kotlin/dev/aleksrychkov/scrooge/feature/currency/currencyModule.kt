@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultObserveAllCurrencies

fun buildCurrencyModule(): NaiveModule {
    return module {
        factory<ObserveAllCurrencies> { DefaultObserveAllCurrencies() }
    }
}
