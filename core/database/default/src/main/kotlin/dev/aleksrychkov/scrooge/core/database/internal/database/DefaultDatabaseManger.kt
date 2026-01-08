package dev.aleksrychkov.scrooge.core.database.internal.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import dev.aleksrychkov.scrooge.core.database.DatabaseManger
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.createDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

internal class DefaultDatabaseManger(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : DatabaseManger, DatabaseProvider {

    private val isInitAllowed: AtomicBoolean = AtomicBoolean(true)
    private val mutex: Mutex by lazy { Mutex() }
    private val driverRef = AtomicReference<SqlDriver?>(null)
    private val databaseRef = AtomicReference<Scrooge?>(null)

    @Suppress("TooGenericExceptionCaught", "ReturnCount")
    private fun getOrCreateDatabase(): Scrooge {
        databaseRef.get()?.let { return it }

        if (!isInitAllowed.get()) error("Database is closed. Call open() explicitly.")

        synchronized(this) {
            databaseRef.get()?.let { return it }

            val d = createDriver(context)
            val db = try {
                Scrooge(d)
            } catch (t: Throwable) {
                d.close()
                throw t
            }
            driverRef.set(d)
            databaseRef.set(db)
            return db
        }
    }

    // region DatabaseManger
    override suspend fun close(): Unit = withContext(ioDispatcher) {
        mutex.withLock {
            isInitAllowed.set(false)
            databaseRef.set(null)
            driverRef.getAndSet(null)?.close()
        }
    }

    override suspend fun open(): Unit = withContext(ioDispatcher) {
        mutex.withLock {
            isInitAllowed.set(true)
            getOrCreateDatabase()
        }
    }

    override suspend fun cleanup() = withContext(ioDispatcher + NonCancellable) {
        val driver = driverRef.get() ?: return@withContext
        driver.execute(null, "DELETE FROM TTransaction", 0).await()
        driver.execute(null, "DELETE FROM Category", 0).await()
        driver.execute(null, "DELETE FROM Tag", 0).await()
        driver.execute(null, "DELETE FROM TransactionTag", 0).await()
        driver.execute(null, "DELETE FROM FilterTag", 0).await()
        driver.execute(null, "DELETE FROM sqlite_sequence", 0).await()
    }
    // endregion DatabaseManger

    // region DatabaseProvider
    override val scrooge: Scrooge
        get() = getOrCreateDatabase()
    // endregion DatabaseProvider
}
