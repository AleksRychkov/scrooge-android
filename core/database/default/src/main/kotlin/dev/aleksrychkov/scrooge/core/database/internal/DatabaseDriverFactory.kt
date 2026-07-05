package dev.aleksrychkov.scrooge.core.database.internal

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.aleksrychkov.scrooge.core.database.Scrooge

internal fun createDriver(context: Context): SqlDriver {
    val ctx = context.applicationContext
    return AndroidSqliteDriver(
        schema = Scrooge.Schema.synchronous(),
        context = ctx,
        name = "Scrooge.db",
        callback = object : AndroidSqliteDriver.Callback(Scrooge.Schema.synchronous()) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                if (dev.aleksrychkov.scrooge.core.database.BuildConfig.DEBUG) {
//                    @Suppress("MagicNumber")
//                    dev.aleksrychkov.scrooge.core.database.internal.debug.PreloadData
//                        .preload(
//                            db = db,
//                            rowsCount = 2500,
//                        )
//                }
            }

            override fun onConfigure(db: SupportSQLiteDatabase) {
                super.onConfigure(db)
                db.enableWriteAheadLogging()
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}
