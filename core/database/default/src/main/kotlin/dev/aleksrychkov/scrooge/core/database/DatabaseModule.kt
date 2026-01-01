package dev.aleksrychkov.scrooge.core.database

import android.content.Context
import dev.aleksrychkov.scrooge.core.database.internal.createDriver
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultCategoryDao
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultReportDao
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultTagDao
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultTransactionDao
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.di.singleton
import kotlinx.coroutines.Dispatchers

fun buildDatabaseModule(context: Context): NaiveModule {
    val writeDispatcher = Dispatchers.IO.limitedParallelism(1)
    val readDispatcher = Dispatchers.IO.limitedParallelism(2)
    return module {
        singleton { Scrooge(createDriver(context)) }
        singleton<TransactionDao> {
            DefaultTransactionDao(
                db = getLazy(),
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        singleton<CategoryDao> {
            DefaultCategoryDao(
                db = getLazy(),
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        singleton<TagDao> {
            DefaultTagDao(
                db = getLazy(),
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        singleton<ReportDao> {
            DefaultReportDao(
                db = getLazy(),
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
    }
}
