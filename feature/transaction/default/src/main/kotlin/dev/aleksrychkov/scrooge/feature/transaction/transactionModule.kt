@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.transaction.internal.DefaultCreateTransactionUseCase
import kotlinx.coroutines.Dispatchers

fun buildTransactionModule(): NaiveModule {
    return module {
        factory<CreateTransactionUseCase> {
            DefaultCreateTransactionUseCase(
                transactionDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
