@file:Suppress("MagicNumber", "NoEmptyFirstLineInMethodBlock")

package dev.aleksrychkov.scrooge.core.database.internal.debug

import androidx.sqlite.db.SupportSQLiteDatabase
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

internal object PreloadData {

    private val incomeCategories: List<Pair<String, Long>> by lazy {
        listOf(
            "Salary" to 9L,
            "Deposit interest" to 10L,
            "Cashback" to 11L,
        )
    }
    private val expenseCategories: List<Pair<String, Long>> by lazy {
        listOf(
            "Groceries" to 1L,
            "Clothes and shoes" to 2L,
            "Fun" to 3L,
            "Taxi" to 4L,
            "Cafe and restaurants" to 5L,
            "Transport" to 6L,
            "Pharmacy" to 7L,
            "Other" to 8L,
        )
    }
    private val currencies: List<String> by lazy {
        listOf(
            CurrencyEntity.RUB.currencyCode,
            CurrencyEntity.EUR.currencyCode,
        )
    }

    fun preload(
        db: SupportSQLiteDatabase,
        rowsCount: Int = 100000,
    ) {
        val (start, end) = getFromToTimestamp()
        val stepMillis = (end - start) / rowsCount

        var stepTimestamp = start
        while (stepTimestamp <= end) {
            val instant = Instant.fromEpochMilliseconds(stepTimestamp)
            if (Random.nextBoolean()) {
                insertExpense(db, Datestamp.from(instant).value)
            } else {
                insertIncome(db, Datestamp.from(instant).value)
            }

            stepTimestamp += stepMillis
        }
    }

    private fun insertIncome(
        db: SupportSQLiteDatabase,
        datestamp: Long,
    ) {
        val amount = Random.nextLong(100, 10000)
        val type = TransactionType.Income.type
        val category = incomeCategories.random().second
        val currencyCode = currencies.random()
        val query =
            """
                INSERT INTO TTransaction (amount, datestamp, type, categoryId, currencyCode) VALUES ($amount, $datestamp, $type, '$category', '$currencyCode');
            """.trimIndent()
        db.execSQL(query)
    }

    private fun insertExpense(
        db: SupportSQLiteDatabase,
        datestamp: Long,
    ) {
        val amount = Random.nextLong(100, 10000)
        val type = TransactionType.Expense.type
        val category = expenseCategories.random().second
        val currencyCode = currencies.random()
        val query =
            """
                INSERT INTO TTransaction (amount, datestamp, type, categoryId, currencyCode) VALUES ($amount, $datestamp, $type, '$category', '$currencyCode');
            """.trimIndent()
        db.execSQL(query)
    }

    private fun getFromToTimestamp(): Pair<Long, Long> {
        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now().toLocalDateTime(tz)
        val startOfYear =
            LocalDateTime(year = now.year - 1, 1, 1, 0, 0)
                .toInstant(tz)
                .toEpochMilliseconds()
        val endOfYear =
            LocalDateTime(year = now.year + 1, 1, 1, 0, 0)
                .toInstant(tz)
                .minus(1, DateTimeUnit.MILLISECOND)
                .toEpochMilliseconds()
        return startOfYear to endOfYear
    }
}
