package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.EnumSet

internal class FiltersReducerTest {

    private val resourceManager = mockk<ResourceManager> {
        every { getStringArray(any()) } returns MONTHS
    }
    private val reducer = FiltersReducer(resourceManager = lazyOf(resourceManager))

    @Test
    fun `When initialized without currency Then automatic resolution is requested`() {
        // Given
        val filter = FilterEntity.currentYear()

        // When
        val result = reducer.reduce(
            event = FiltersEvent.External.Init(
                filter = filter,
                settings = EnumSet.of(FiltersSettings.Years, FiltersSettings.Currency),
            ),
            state = FiltersState(),
        )

        // Then
        assertEquals(CurrencySelectionMode.Automatic, result.state.currencySelectionMode)
        assertTrue(result.commands.contains(FiltersCommand.ResolveCurrency(filter = filter)))
    }

    @Test
    fun `When initialized with currency Then manual selection is preserved`() {
        // Given
        val filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.USD)

        // When
        val result = reducer.reduce(
            event = FiltersEvent.External.Init(
                filter = filter,
                settings = EnumSet.of(FiltersSettings.Years, FiltersSettings.Currency),
            ),
            state = FiltersState(),
        )

        // Then
        assertEquals(CurrencySelectionMode.Manual, result.state.currencySelectionMode)
        assertFalse(result.commands.any { it is FiltersCommand.ResolveCurrency })
    }

    @Test
    fun `When automatic result follows manual selection Then manual currency is preserved`() {
        // Given
        val filter = FilterEntity.currentYear()
        val manualState = FiltersState(
            filter = filter.copy(currency = CurrencyEntity.EUR),
            currencySelectionMode = CurrencySelectionMode.Manual,
        )

        // When
        val result = reducer.reduce(
            event = FiltersEvent.Internal.SetAutomaticCurrency(
                filter = filter,
                currency = CurrencyEntity.USD,
                submitWhenResolved = false,
            ),
            state = manualState,
        )

        // Then
        assertEquals(CurrencyEntity.EUR, result.state.filter.currency)
    }

    @Test
    fun `When automatic result is stale Then currency is unchanged`() {
        // Given
        val currentFilter = FilterEntity.currentYear()
        val staleFilter = FilterEntity.currentMonth()
        val state = FiltersState(
            filter = currentFilter,
            currencySelectionMode = CurrencySelectionMode.Automatic,
        )

        // When
        val result = reducer.reduce(
            event = FiltersEvent.Internal.SetAutomaticCurrency(
                filter = staleFilter,
                currency = CurrencyEntity.USD,
                submitWhenResolved = false,
            ),
            state = state,
        )

        // Then
        assertNull(result.state.filter.currency)
    }

    @Test
    fun `When currency is cleared Then selection is manually empty`() {
        // Given
        val state = FiltersState(
            filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.USD),
            currencySelectionMode = CurrencySelectionMode.Manual,
        )

        // When
        val result = reducer.reduce(
            event = FiltersEvent.External.RemoveCurrency,
            state = state,
        )

        // Then
        assertNull(result.state.filter.currency)
        assertEquals(CurrencySelectionMode.Manual, result.state.currencySelectionMode)
    }

    @Test
    fun `When reset currency resolves Then completed filter is submitted`() {
        // Given
        val state = FiltersState(
            settings = EnumSet.of(FiltersSettings.Years, FiltersSettings.Currency),
            filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.USD),
            currencySelectionMode = CurrencySelectionMode.Manual,
        )

        // When
        val resetResult = reducer.reduce(FiltersEvent.External.Reset, state)
        val command = resetResult.commands.single() as FiltersCommand.ResolveCurrency
        val resolvedResult = reducer.reduce(
            event = FiltersEvent.Internal.SetAutomaticCurrency(
                filter = command.filter,
                currency = CurrencyEntity.RUB,
                submitWhenResolved = command.submitWhenResolved,
            ),
            state = resetResult.state,
        )

        // Then
        assertTrue(command.submitWhenResolved)
        assertTrue(resetResult.effects.isEmpty())
        assertEquals(CurrencySelectionMode.Automatic, resetResult.state.currencySelectionMode)
        assertEquals(CurrencyEntity.RUB, resolvedResult.state.filter.currency)
        assertEquals(1, resolvedResult.effects.size)
    }

    private companion object {
        val MONTHS: Array<String> = Array(12) { index -> "Month $index" }
    }
}
