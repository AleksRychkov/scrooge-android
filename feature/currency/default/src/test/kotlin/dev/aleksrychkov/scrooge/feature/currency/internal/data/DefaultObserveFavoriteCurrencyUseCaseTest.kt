package dev.aleksrychkov.scrooge.feature.currency.internal.data

import dev.aleksrychkov.scrooge.feature.currency.ObserveFavoriteCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultObserveFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.FavoriteCurrencyRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DefaultObserveFavoriteCurrencyUseCaseTest {
    private val repository: FavoriteCurrencyRepository = mockk(relaxed = true)
    private val useCase = DefaultObserveFavoriteCurrencyUseCase(lazy { repository })

    @Test
    fun `When invoke Then result is Success`() = runTest {
        // Given
        coEvery { repository.observeFavoriteCurrencies() } returns emptyFlow()
        // When
        val result = useCase.invoke()
        // Then
        assertTrue(result is ObserveFavoriteCurrencyResult.Success)
    }

    @Test
    fun `Given repository throws When invoke Then result is Failure`() = runTest {
        // Given
        coEvery { repository.observeFavoriteCurrencies() } throws RuntimeException("Test")
        // When
        val result = useCase.invoke()
        // Then
        assertEquals(ObserveFavoriteCurrencyResult.Failure, result)
    }
}
