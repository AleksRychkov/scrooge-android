package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity
import dev.aleksrychkov.scrooge.core.entity.ReportCategoryTimelineEntity
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportCategoryTimelineResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ReportTimelineUseCaseTest {

    private val reportDao = mockk<ReportDao>()

    @Test
    fun `When balance timeline loads Then success contains DAO result`() = runTest {
        // Given
        val filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.RUB)
        val entity = ReportBalanceTimelineEntity()
        coEvery { reportDao.balanceTimeline(filter) } returns entity
        val useCase = DefaultReportBalanceTimelineUseCase(
            reportDao = lazyOf(reportDao),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )

        // When
        val result = useCase(filter)

        // Then
        assertEquals(ReportBalanceTimelineResult.Success(entity), result)
    }

    @Test
    fun `When balance timeline fails Then result is failure`() = runTest {
        // Given
        val filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.RUB)
        coEvery { reportDao.balanceTimeline(filter) } throws IllegalStateException()
        val useCase = DefaultReportBalanceTimelineUseCase(
            reportDao = lazyOf(reportDao),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )

        // When
        val result = useCase(filter)

        // Then
        assertEquals(ReportBalanceTimelineResult.Failure, result)
    }

    @Test
    fun `When category timeline loads Then success contains DAO result`() = runTest {
        // Given
        val filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.RUB)
        val entity = ReportCategoryTimelineEntity()
        coEvery { reportDao.categoryTimeline(filter) } returns entity
        val useCase = DefaultReportCategoryTimelineUseCase(
            reportDao = lazyOf(reportDao),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )

        // When
        val result = useCase(filter)

        // Then
        assertEquals(ReportCategoryTimelineResult.Success(entity), result)
    }

    @Test
    fun `When category timeline fails Then result is failure`() = runTest {
        // Given
        val filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.RUB)
        coEvery { reportDao.categoryTimeline(filter) } throws IllegalStateException()
        val useCase = DefaultReportCategoryTimelineUseCase(
            reportDao = lazyOf(reportDao),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )

        // When
        val result = useCase(filter)

        // Then
        assertEquals(ReportCategoryTimelineResult.Failure, result)
    }
}
