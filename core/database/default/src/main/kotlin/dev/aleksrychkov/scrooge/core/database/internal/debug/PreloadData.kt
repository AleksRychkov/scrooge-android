@file:Suppress("MagicNumber", "NoEmptyFirstLineInMethodBlock")

package dev.aleksrychkov.scrooge.core.database.internal.debug

import androidx.sqlite.db.SupportSQLiteDatabase
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock

internal object PreloadData {

    private val incomeCategories: List<Pair<String, String>> by lazy {
        listOf(
            "Salary" to "Payments",
            "Deposit interest" to "AccountBalance",
            "Cashback" to "Redeem",
        )
    }
    private val expenseCategories: List<Pair<String, String>> by lazy {
        listOf(
            "Groceries" to "ShoppingBasket",
            "Clothes and shoes" to "Checkroom",
            "Fun" to "Attractions",
            "Taxi" to "LocalTaxi",
            "Cafe and restaurants" to "Restaurant",
            "Transport" to "DirectionsBus",
            "Pharmacy" to "LocalPharmacy",
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
            if (Random.nextBoolean()) {
                insertExpense(db, stepTimestamp)
            } else {
                insertIncome(db, stepTimestamp)
            }

            stepTimestamp += stepMillis
        }
    }

    private fun insertIncome(
        db: SupportSQLiteDatabase,
        timestamp: Long,
    ) {
        val amount = Random.nextLong(100, 10000)
        val type = TransactionType.Income.type
        val (category, categoryIcon) = incomeCategories.random()
        val tags = ""
        val currencyCode = currencies.random()
        val query =
            """
                INSERT INTO TTransaction (amount, timestamp, type, category, categoryIconId, tags, currencyCode) VALUES ($amount, $timestamp, $type, '$category', '$categoryIcon', '$tags', '$currencyCode');
            """.trimIndent()
        db.execSQL(query)
    }

    private fun insertExpense(
        db: SupportSQLiteDatabase,
        timestamp: Long,
    ) {
        val amount = Random.nextLong(100, 10000)
        val type = TransactionType.Expense.type
        val (category, categoryIcon) = expenseCategories.random()
        val tags = ""
        val currencyCode = currencies.random()
        val query =
            """
                INSERT INTO TTransaction (amount, timestamp, type, category, categoryIconId, tags, currencyCode) VALUES ($amount, $timestamp, $type, '$category', '$categoryIcon', '$tags', '$currencyCode');
            """.trimIndent()
        db.execSQL(query)
    }

    private fun getFromToTimestamp(): Pair<Long, Long> {
        val tz = TimeZone.currentSystemDefault()
        val now = Clock.System.now().toLocalDateTime(tz)
        val startOfYear =
            LocalDateTime(year = now.year, 1, 1, 0, 0)
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
