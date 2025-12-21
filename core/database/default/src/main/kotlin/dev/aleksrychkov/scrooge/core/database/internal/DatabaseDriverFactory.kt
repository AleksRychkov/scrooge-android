package dev.aleksrychkov.scrooge.core.database.internal

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.aleksrychkov.scrooge.core.database.BuildConfig
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.debug.PreloadData

internal fun createDriver(context: Context): SqlDriver {
    val ctx = context.applicationContext
    return AndroidSqliteDriver(
        schema = Scrooge.Companion.Schema.synchronous(),
        context = ctx,
        name = "Scrooge.db",
        callback = object :
            AndroidSqliteDriver.Callback(Scrooge.Companion.Schema.synchronous()) {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                if (BuildConfig.DEBUG) {
                    @Suppress("MagicNumber")
                    PreloadData.preload(db, 25000)
                }
            }

            override fun onConfigure(db: SupportSQLiteDatabase) {
                super.onConfigure(db)
                db.enableWriteAheadLogging()
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )
}
