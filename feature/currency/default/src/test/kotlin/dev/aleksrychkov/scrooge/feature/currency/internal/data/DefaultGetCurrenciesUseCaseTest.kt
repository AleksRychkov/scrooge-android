package dev.aleksrychkov.scrooge.feature.currency.internal.data

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.internal.DefaultGetCurrenciesUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultGetCurrenciesUseCaseTest {
    private val useCase = DefaultGetCurrenciesUseCase()

    @Test
    fun `When invoke Then currencies returned`() = runTest {
        // Given
        val expected = CurrencyEntity.entries.toImmutableList()
        // When
        val actual = useCase.invoke()
        // Then
        assertEquals(expected, actual)
    }
}
