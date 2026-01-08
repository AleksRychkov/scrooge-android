package dev.aleksrychkov.scrooge.core.database

import android.content.Context
import dev.aleksrychkov.scrooge.core.database.fileadapter.DatabaseFileAdapter
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultCategoryDao
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultReportDao
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultTagDao
import dev.aleksrychkov.scrooge.core.database.internal.dao.DefaultTransactionDao
import dev.aleksrychkov.scrooge.core.database.internal.database.DefaultDatabaseManger
import dev.aleksrychkov.scrooge.core.database.internal.fileadapter.DefaultDatabaseFileAdapter
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.di.singleton
import kotlinx.coroutines.Dispatchers

fun buildDatabaseModule(context: Context): NaiveModule {
    val writeDispatcher = Dispatchers.IO.limitedParallelism(1)
    val readDispatcher = Dispatchers.IO.limitedParallelism(2)
    return module {
        val dbManager = DefaultDatabaseManger(context = context, ioDispatcher = Dispatchers.IO)
        singleton<DatabaseManger> { dbManager }
        singleton<TransactionDao> {
            DefaultTransactionDao(
                dbProvider = dbManager,
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        singleton<CategoryDao> {
            DefaultCategoryDao(
                dbProvider = dbManager,
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        singleton<TagDao> {
            DefaultTagDao(
                dbProvider = dbManager,
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        singleton<ReportDao> {
            DefaultReportDao(
                dbProvider = dbManager,
                readDispatcher = readDispatcher,
                writeDispatcher = writeDispatcher,
            )
        }
        factory<DatabaseFileAdapter> {
            DefaultDatabaseFileAdapter(
                dbProvider = dbManager,
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
