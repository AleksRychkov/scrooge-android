package dev.aleksrychkov.scrooge.feature.currency.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.internal.data.source.LastUsedCurrencySource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

internal class LastUsedCurrencyRepositoryTest {

    private val source: LastUsedCurrencySource = mockk(relaxed = true)

    @Test
    fun `When setLastUsedCurrency Then source set method invoked`() = runTest {
        // Given
        val repository = LastUsedCurrencyRepository(lazy { source })
        val currency = CurrencyEntity.USD
        coEvery { source.set(currency) } just Runs
        // When
        repository.setLastUsedCurrency(currency)
        // Then
        coVerify(exactly = 1) { source.set(currency) }
    }

    @Test
    fun `When getLastUsedCurrency returns currency then repository returns same`() = runTest {
        // Given
        val repository = LastUsedCurrencyRepository(lazy { source })
        val currency = CurrencyEntity.EUR
        coEvery { source.get() } returns currency
        // When
        val result = repository.getLastUsedCurrency()
        // Then
        coVerify(exactly = 1) { source.get() }
        assertEquals(currency, result)
    }

    @Test
    fun `When getLastUsedCurrency throws exception Then repository throws exception`() = runTest {
        // Given
        val repository = LastUsedCurrencyRepository(lazy { source })
        coEvery { source.get() } throws RuntimeException("Test exception")
        // When
        val result = try {
            repository.getLastUsedCurrency()
            null
        } catch (e: RuntimeException) {
            e
        }
        // Then
        coVerify(exactly = 1) { source.get() }
        assertNotNull(result)
    }
}
