package dev.aleksrychkov.scrooge.core.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReportQueriesTest {

    private lateinit var driver: JdbcSqliteDriver
    private lateinit var database: Scrooge

    @BeforeEach
    fun setUp() = runTest {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Scrooge.Schema.create(driver).await()
        database = Scrooge(driver)
        database.categoryQueries.add(
            id = 1,
            name = "Salary",
            type = INCOME,
            iconId = "salary",
            color = 1,
            orderIndex = 0,
        )
        database.categoryQueries.add(
            id = 2,
            name = "Food",
            type = EXPENSE,
            iconId = "food",
            color = 2,
            orderIndex = 0,
        )
    }

    @AfterEach
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `When currencies have equal transaction counts Then alphabetically first currency is selected`() = runTest {
        // Given
        addTransaction(amount = 100, datestamp = 20260101, type = INCOME, categoryId = 1, currency = "USD")
        addTransaction(amount = 100, datestamp = 20260102, type = INCOME, categoryId = 1, currency = "EUR")

        // When
        val currency = database.transactionQueries.selectMostUsedCurrency(
            fromDatestamp = 20260101,
            toDatestamp = 20260131,
            categoryId = null,
            transactionType = null,
        ).executeAsOne()

        // Then
        assertEquals("EUR", currency)
    }

    @Test
    fun `When currency usage is filtered Then the most frequent matching currency is selected`() = runTest {
        // Given
        addTransaction(amount = 100, datestamp = 20260101, type = INCOME, categoryId = 1, currency = "USD")
        addTransaction(amount = 100, datestamp = 20260102, type = INCOME, categoryId = 1, currency = "USD")
        addTransaction(amount = 50, datestamp = 20260103, type = EXPENSE, categoryId = 2, currency = "EUR")
        addTransaction(amount = 50, datestamp = 20260201, type = INCOME, categoryId = 1, currency = "EUR")

        // When
        val currency = database.transactionQueries.selectMostUsedCurrency(
            fromDatestamp = 20260101,
            toDatestamp = 20260131,
            categoryId = 1,
            transactionType = INCOME,
        ).executeAsOne()

        // Then
        assertEquals("USD", currency)
    }

    @Test
    fun `When category usage is filtered Then most frequent matching category is selected`() = runTest {
        // Given
        addTransaction(amount = 10, datestamp = 20260101, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 20, datestamp = 20260102, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 30, datestamp = 20260103, type = INCOME, categoryId = 1, currency = "RUB")
        addTransaction(amount = 40, datestamp = 20260104, type = EXPENSE, categoryId = 2, currency = "USD")

        // When
        val category = database.transactionQueries.selectMostUsedCategory(
            fromDatestamp = 20260101,
            toDatestamp = 20260131,
            transactionType = EXPENSE,
            currencyCode = "RUB",
        ).executeAsOne()

        // Then
        assertEquals("Food", category.name)
    }

    @Test
    fun `When random category is filtered by type Then matching category is returned`() = runTest {
        // Given
        val type = INCOME

        // When
        val category = database.categoryQueries.selectRandom(type).executeAsOne()

        // Then
        assertEquals("Salary", category.name)
    }

    @Test
    fun `When balance timeline is queried Then it subtracts expenses and isolates currency`() = runTest {
        // Given
        addTransaction(amount = 500, datestamp = 20260101, type = INCOME, categoryId = 1, currency = "RUB")
        addTransaction(amount = 120, datestamp = 20260102, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 70, datestamp = 20260202, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 200, datestamp = 20260302, type = INCOME, categoryId = 1, currency = "RUB")
        addTransaction(amount = 999, datestamp = 20260103, type = INCOME, categoryId = 1, currency = "USD")
        database.tagQueries.insertFilterTag(999)

        // When
        val rows = database.reportQueries.balanceTimeline(
            fromDatestamp = 20260101,
            toDatestamp = 20260331,
            currencyCode = "RUB",
        ).executeAsList()

        // Then
        assertEquals(listOf(380L, -70L, 200L), rows.map { it.balance })
    }

    @Test
    fun `When category timeline is queried Then it groups matching category by month`() = runTest {
        // Given
        addTransaction(amount = 40, datestamp = 20260101, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 60, datestamp = 20260120, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 80, datestamp = 20260201, type = EXPENSE, categoryId = 2, currency = "RUB")
        addTransaction(amount = 90, datestamp = 20260102, type = EXPENSE, categoryId = 2, currency = "USD")

        // When
        val rows = database.reportQueries.categoryTimeline(
            fromDatestamp = 20260101,
            toDatestamp = 20260228,
            categoryId = 2,
            transactionType = EXPENSE,
            currencyCode = "RUB",
        ).executeAsList()

        // Then
        assertEquals(listOf(100L, 80L), rows.map { it.total })
        assertTrue(rows.all { it.categoryName == "Food" && it.currencyCode == "RUB" })
    }

    private suspend fun addTransaction(
        amount: Long,
        datestamp: Long,
        type: Long,
        categoryId: Long,
        currency: String,
    ) {
        database.transactionQueries.create(
            amount = amount,
            datestamp = datestamp,
            type = type,
            categoryId = categoryId,
            currencyCode = currency,
            comment = null,
        )
    }

    private companion object {
        const val INCOME = 0L
        const val EXPENSE = 1L
    }
}
