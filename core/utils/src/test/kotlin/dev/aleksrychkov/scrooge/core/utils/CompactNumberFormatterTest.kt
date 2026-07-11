package dev.aleksrychkov.scrooge.core.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CompactNumberFormatterTest {

    @Test
    fun `When value is below thousand Then value has no suffix`() {
        // Given
        val value = 999.0

        // When
        val result = formatCompactNumber(value)

        // Then
        assertEquals("999", result)
    }

    @Test
    fun `When value is thousand Then value uses K suffix`() {
        // Given
        val value = 1_000.0

        // When
        val result = formatCompactNumber(value)

        // Then
        assertEquals("1K", result)
    }

    @Test
    fun `When value contains useful fraction Then one decimal is retained`() {
        // Given
        val value = 1_500.0

        // When
        val result = formatCompactNumber(value)

        // Then
        assertEquals("1.5K", result)
    }

    @Test
    fun `When value is negative million Then sign and M suffix are retained`() {
        // Given
        val value = -2_000_000.0

        // When
        val result = formatCompactNumber(value)

        // Then
        assertEquals("-2M", result)
    }
}
