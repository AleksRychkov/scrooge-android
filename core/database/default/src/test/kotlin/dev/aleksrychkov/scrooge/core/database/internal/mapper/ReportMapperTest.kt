package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.BalanceTimeline
import dev.aleksrychkov.scrooge.core.database.CategoryTimeline
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReportMapperTest {

    @Test
    fun `When balance months are sparse Then missing months are zero filled`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 3, 31))
        val rows = listOf(
            BalanceTimeline(year = 2025, month = 1, balance = 100),
            BalanceTimeline(year = 2025, month = 3, balance = -30),
        )

        // When
        val result = ReportMapper.balanceTimelineToEntity(list = rows, period = period)

        // Then
        assertEquals(listOf(100L, 0L, -30L), result.points.map { it.amount })
        assertEquals(
            listOf(LocalDate(2025, 1, 1), LocalDate(2025, 2, 1), LocalDate(2025, 3, 1)),
            result.points.map { it.month },
        )
    }

    @Test
    fun `When balance rows are empty Then timeline is empty`() {
        // Given
        val period = period(from = LocalDate(2025, 1, 1), to = LocalDate(2025, 3, 31))

        // When
        val result = ReportMapper.balanceTimelineToEntity(list = emptyList(), period = period)

        // Then
        assertTrue(result.points.isEmpty())
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
