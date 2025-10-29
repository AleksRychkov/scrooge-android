package dev.aleksrychkov.scrooge.feature.currency.internal.data

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultAddToFavoriteCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.FavoriteCurrencyRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DefaultAddToFavoriteCurrencyUseCaseTest {

    private val repository: FavoriteCurrencyRepository = mockk(relaxed = true)
    private val useCase = DefaultAddToFavoriteCurrencyUseCase(lazy { repository })

    @Test
    fun `When invoke Then repository called`() = runTest {
        // Given
        coEvery { repository.addFavoriteCurrency(any()) } just Runs
        // When
        useCase.invoke(CurrencyEntity.RUB)
        // Then
        coVerify(exactly = 1) { repository.addFavoriteCurrency(any()) }
    }
}
