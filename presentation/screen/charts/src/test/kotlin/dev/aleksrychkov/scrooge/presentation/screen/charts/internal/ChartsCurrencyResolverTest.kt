package dev.aleksrychkov.scrooge.presentation.screen.charts.internal

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChartsCurrencyResolverTest {

    private val mostUsed = mockk<GetMostUsedCurrencyUseCase>()
    private val lastUsed = mockk<GetLastUsedCurrencyUseCase>()
    private val resolver = ChartsCurrencyResolver(lazyOf(mostUsed), lazyOf(lastUsed))

    @Test
    fun `When period has transactions Then most used currency is returned`() = runTest {
        // Given
        val filter = FilterEntity.currentYear()
        coEvery { mostUsed(filter) } returns GetMostUsedCurrencyResult.Success(CurrencyEntity.USD)

        // When
        val currency = resolver(filter)

        // Then
        assertEquals(CurrencyEntity.USD, currency)
        coVerify(exactly = 0) { lastUsed() }
    }

    @Test
    fun `When period is empty Then last used currency is returned`() = runTest {
        // Given
        val filter = FilterEntity.currentYear()
        coEvery { mostUsed(filter) } returns GetMostUsedCurrencyResult.Empty
        coEvery { lastUsed() } returns GetLastUsedCurrencyResult.Success(CurrencyEntity.EUR)

        // When
        val currency = resolver(filter)

        // Then
        assertEquals(CurrencyEntity.EUR, currency)
    }

    @Test
    fun `When period and history are empty Then RUB is returned`() = runTest {
        // Given
        val filter = FilterEntity.currentYear()
        coEvery { mostUsed(filter) } returns GetMostUsedCurrencyResult.Empty
        coEvery { lastUsed() } returns GetLastUsedCurrencyResult.Empty

        // When
        val currency = resolver(filter)

        // Then
        assertEquals(CurrencyEntity.RUB, currency)
    }
}
