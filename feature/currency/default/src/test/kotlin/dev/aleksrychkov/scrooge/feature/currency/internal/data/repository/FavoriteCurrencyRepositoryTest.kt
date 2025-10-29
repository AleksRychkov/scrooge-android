package dev.aleksrychkov.scrooge.feature.currency.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.internal.data.source.FavoriteCurrencySource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class FavoriteCurrencyRepositoryTest {

    private val source: FavoriteCurrencySource = mockk(relaxed = true)

    @Test
    fun `When addFavoriteCurrency Then currency added to favorite`() = runTest {
        // Given
        val repository = FavoriteCurrencyRepository(lazy { source })
        val entity = CurrencyEntity.RUB
        coEvery { source.add(entity) } just Runs
        coEvery { source.get() } returns persistentListOf(entity)
        // When
        repository.addFavoriteCurrency(entity)
        // Then
        coVerify(exactly = 1) { source.add(entity) }
        coVerify(exactly = 1) { source.get() }
        val observed = repository.observeFavoriteCurrencies().firstOrNull()?.firstOrNull()
        assertEquals(entity, observed)
    }

    @Test
    fun `When removeFavoriteCurrency Then currency removed from favorite`() = runTest {
        // Given
        val repository = FavoriteCurrencyRepository(lazy { source })
        val entity = CurrencyEntity.RUB
        coEvery { source.remove(entity) } just Runs
        coEvery { source.add(entity) } just Runs
        coEvery { source.get() } returns persistentListOf(entity) andThen persistentListOf()
        repository.addFavoriteCurrency(entity)
        var observed = repository.observeFavoriteCurrencies().firstOrNull()?.firstOrNull()
        assertEquals(entity, observed)
        // When
        repository.removeFavoriteCurrency(entity)
        // Then
        coVerify(exactly = 1) { source.remove(entity) }
        observed = repository.observeFavoriteCurrencies().firstOrNull()?.firstOrNull()
        assertNull(observed)
    }
}
