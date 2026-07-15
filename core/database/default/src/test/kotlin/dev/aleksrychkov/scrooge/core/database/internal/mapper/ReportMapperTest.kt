package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.BalanceTotalTimeline
import dev.aleksrychkov.scrooge.core.database.CategoryTimeline
import dev.aleksrychkov.scrooge.core.database.IncomeExpenseTimeline
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReportMapperTest {

    @Test
    fun `When total balance is mapped Then transactions before period form opening balance`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 3, 31))
        val rows = listOf(
            BalanceTotalTimeline(year = 2024, month = 12, balance = 1_000),
            BalanceTotalTimeline(year = 2025, month = 2, balance = -200),
            BalanceTotalTimeline(year = 2025, month = 3, balance = 500),
        )

        // When
        val result = ReportMapper.balanceTotalTimelineToEntity(
            list = rows,
            period = period,
            currentDate = LocalDate(2025, 3, 15),
        )

        // Then
        assertEquals(listOf(1_000L, 800L, 1_300L), result.points.map { it.amount })
    }

    @Test
    fun `When income expense months are sparse Then missing months are zero filled`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 3, 31))
        val rows = listOf(
            IncomeExpenseTimeline(year = 2025, month = 1, income = 500, expense = 200),
            IncomeExpenseTimeline(year = 2025, month = 3, income = 100, expense = 300),
        )

        // When
        val result = IncomeExpenseTimelineMapper.toEntity(rows, period)

        // Then
        assertEquals(listOf(500L, 0L, 100L), result.points.map { it.income })
        assertEquals(listOf(200L, 0L, 300L), result.points.map { it.expense })
    }

    @Test
    fun `When income expense period extends into future Then future months are omitted`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 12, 31))
        val rows = listOf(
            IncomeExpenseTimeline(year = 2025, month = 1, income = 500, expense = 200),
        )

        // When
        val result = IncomeExpenseTimelineMapper.toEntity(
            list = rows,
            period = period,
            currentDate = LocalDate(2025, 2, 15),
        )

        // Then
        assertEquals(
            listOf(LocalDate(2025, 1, 1), LocalDate(2025, 2, 1)),
            result.points.map { it.month },
        )
    }

    @Test
    fun `When category months are sparse Then every series contains all months`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 3, 31))
        val rows = listOf(
            categoryRow(month = 1, total = 10),
            categoryRow(month = 3, total = 20),
        )

        // When
        val result = ReportMapper.categoryTimelineToEntity(list = rows, period = period)

        // Then
        assertEquals(1, result.series.size)
        assertEquals(listOf(10L, 0L, 20L), result.series.single().points.map { it.amount })
    }

    @Test
    fun `When category rows are empty Then category timeline is empty`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 3, 31))

        // When
        val result = ReportMapper.categoryTimelineToEntity(list = emptyList(), period = period)

        // Then
        assertTrue(result.series.isEmpty())
    }

    @Test
    fun `When category period extends into future Then future months are omitted`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 12, 31))
        val rows = listOf(categoryRow(month = 1, total = 10))

        // When
        val result = ReportMapper.categoryTimelineToEntity(
            list = rows,
            period = period,
            currentDate = LocalDate(2025, 2, 15),
        )

        // Then
        assertEquals(
            listOf(LocalDate(2025, 1, 1), LocalDate(2025, 2, 1)),
            result.series.single().points.map { it.month },
        )
    }

    private fun categoryRow(month: Long, total: Long): CategoryTimeline =
        CategoryTimeline(
            year = 2025,
            month = month,
            type = 1,
            currencyCode = "RUB",
            total = total,
            categoryId = 1,
            categoryName = "Food",
            categoryIconId = "ShoppingBasket",
            categoryType = 1,
            categoryColor = 1,
        )

    private fun period(from: LocalDate, to: LocalDate): PeriodDatestampEntity =
        PeriodDatestampEntity(from = Datestamp.from(from), to = Datestamp.from(to))
}
