package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.EnumSet

internal class FiltersReducerTest {

    private val resourceManager = mockk<ResourceManager> {
        every { getStringArray(any()) } returns MONTHS
    }
    private val reducer = FiltersReducer(resourceManager = lazyOf(resourceManager))

    @Test
    fun `When initialized without currency Then supplied filter is preserved`() {
        // Given
        val filter = FilterEntity.currentYear()

        // When
        val result = reducer.reduce(
            FiltersEvent.External.Init(
                filter = filter,
                settings = EnumSet.of(FiltersSettings.Years, FiltersSettings.Currency),
            ),
            FiltersState(),
        )

        // Then
        assertEquals(filter, result.state.filter)
        assertEquals(listOf(FiltersCommand.GetFiltersStartEndYears), result.commands)
    }

    @Test
    fun `When currency is selected Then selected currency is stored`() {
        // Given
        val state = FiltersState(filter = FilterEntity.currentYear())

        // When
        val result = reducer.reduce(FiltersEvent.External.SetCurrency(CurrencyEntity.USD), state)

        // Then
        assertEquals(CurrencyEntity.USD, result.state.filter.currency)
        assertEquals(emptyList<FiltersCommand>(), result.commands)
    }

    @Test
    fun `When currency is cleared Then currency becomes null`() {
        // Given
        val state = FiltersState(
            filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.USD),
        )

        // When
        val result = reducer.reduce(FiltersEvent.External.RemoveCurrency, state)

        // Then
        assertNull(result.state.filter.currency)
        assertEquals(emptyList<FiltersCommand>(), result.commands)
    }

    @Test
    fun `When filters are reset Then plain filter is submitted`() {
        // Given
        val state = FiltersState(
            settings = EnumSet.of(FiltersSettings.Years, FiltersSettings.Currency),
            filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.USD),
        )

        // When
        val result = reducer.reduce(FiltersEvent.External.Reset, state)

        // Then
        val submit = result.effects.single() as FiltersEffect.SubmitFilters
        assertNull(result.state.filter.currency)
        assertEquals(result.state.filter, submit.filter)
        assertEquals(emptyList<FiltersCommand>(), result.commands)
    }

    private companion object {
        val MONTHS: Array<String> = Array(12) { index -> "Month $index" }
    }
}
