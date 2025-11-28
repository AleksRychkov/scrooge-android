@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.transaction.internal.DefaultCreateTransactionUseCase
import dev.aleksrychkov.scrooge.feature.transaction.internal.DefaultDeleteTransactionUseCase
import dev.aleksrychkov.scrooge.feature.transaction.internal.DefaultEditTransactionUseCase
import dev.aleksrychkov.scrooge.feature.transaction.internal.DefaultGetTransactionUseCase
import dev.aleksrychkov.scrooge.feature.transaction.internal.DefaultGetTransactionsUseCase
import kotlinx.coroutines.Dispatchers

fun buildTransactionModule(): NaiveModule {
    return module {
        factory<CreateTransactionUseCase> {
            DefaultCreateTransactionUseCase(
                transactionDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<GetTransactionsUseCase> {
            DefaultGetTransactionsUseCase(
                transactionDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<GetTransactionUseCase> {
            DefaultGetTransactionUseCase(
                transactionDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<EditTransactionUseCase> {
            DefaultEditTransactionUseCase(
                transactionDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<DeleteTransactionUseCase> {
            DefaultDeleteTransactionUseCase(
                transactionDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
