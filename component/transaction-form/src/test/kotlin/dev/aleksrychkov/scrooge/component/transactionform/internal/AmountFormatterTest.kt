package dev.aleksrychkov.scrooge.component.transactionform.internal

import dev.aleksrychkov.scrooge.component.transactionform.internal.utils.AmountFormatter
import dev.aleksrychkov.scrooge.core.entity.DELIMITER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class AmountFormatterTest {

    companion object {
        @JvmStatic
        fun sanitizeValueInputData(): List<Arguments> = listOf(
            Arguments.of("", ""),
            Arguments.of(" ", ""),
            Arguments.of("0", "0"),
            Arguments.of(",", ""),
            Arguments.of("00", "0"),
            Arguments.of("001", "1"),
            Arguments.of("100", "100"),
            Arguments.of("1.", "1$DELIMITER"),
            Arguments.of("0.", "0$DELIMITER"),
            Arguments.of("00000.", "0$DELIMITER"),
            Arguments.of(".", ""),
            Arguments.of("1..", "1$DELIMITER"),
            Arguments.of("1.0.", "1${DELIMITER}0"),
            Arguments.of("1.0.00", "1${DELIMITER}0"),
            Arguments.of("1,", "1$DELIMITER"),
            Arguments.of("01.", "1$DELIMITER"),
            Arguments.of("1.0", "1${DELIMITER}0"),
            Arguments.of("1.10", "1${DELIMITER}10"),
            Arguments.of("1.101", "1${DELIMITER}10"),
        )
    }

    @ParameterizedTest(name = "Given {0} When sanitizeValue Then {1}")
    @MethodSource("sanitizeValueInputData")
    fun `format test`(input: String, expected: String) {
        // Given
        // When
        val actual = AmountFormatter.sanitizeValue(input)
        // Then
        assertEquals(expected, actual)
    }
}
