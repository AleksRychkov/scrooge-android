package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal

import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class AmountInputTransformationTest {
    companion object {
        @JvmStatic
        fun testData(): Stream<Arguments> =
            Stream.of(
                // empty
                Arguments.of("", ""),

                // spaces
                Arguments.of("  0   $AMOUNT_DELIMITER  1", "0${AMOUNT_DELIMITER}1"),

                // single digits
                Arguments.of("0", "0"),
                Arguments.of("1", "1"),
                Arguments.of("9", "9"),

                // leading delimiter
                Arguments.of(".", "0$AMOUNT_DELIMITER"),
                Arguments.of(",", "0$AMOUNT_DELIMITER"),

                // duplicate delimiters
                Arguments.of("..", "0$AMOUNT_DELIMITER"),
                Arguments.of(",,", "0$AMOUNT_DELIMITER"),
                Arguments.of("0..", "0$AMOUNT_DELIMITER"),
                Arguments.of("1..", "1$AMOUNT_DELIMITER"),

                // leading zeros
                Arguments.of("00", "0"),
                Arguments.of("000", "0"),
                Arguments.of("0001", "1"),
                Arguments.of("00012", "12"),

                // simple integers
                Arguments.of("10", "10"),
                Arguments.of("100", "100"),
                Arguments.of("1000", "1 000"),
                Arguments.of("10000", "10 000"),
                Arguments.of("100000", "100 000"),
                Arguments.of("1000000", "1 000 000"),

                // grouping with non-zero numbers
                Arguments.of("1234", "1 234"),
                Arguments.of("12345", "12 345"),
                Arguments.of("123456", "123 456"),
                Arguments.of("1234567", "1 234 567"),

                // decimal values
                Arguments.of("0.1", "0${AMOUNT_DELIMITER}1"),
                Arguments.of("1.1", "1${AMOUNT_DELIMITER}1"),
                Arguments.of("10.12", "10${AMOUNT_DELIMITER}12"),
                Arguments.of("1000.12", "1 000${AMOUNT_DELIMITER}12"),

                // decimal with comma input
                Arguments.of("1,1", "1${AMOUNT_DELIMITER}1"),
                Arguments.of("1000,12", "1 000${AMOUNT_DELIMITER}12"),

                // duplicate decimals
                Arguments.of("1.2.3", "1${AMOUNT_DELIMITER}23"),
                Arguments.of("1,2,3", "1${AMOUNT_DELIMITER}23"),
                Arguments.of("1.,23", "1${AMOUNT_DELIMITER}23"),

                // zeros with decimal
                Arguments.of("0.0", "0${AMOUNT_DELIMITER}0"),
                Arguments.of("000.1", "0${AMOUNT_DELIMITER}1"),
                Arguments.of("0001.2", "1${AMOUNT_DELIMITER}2"),

                // grouping + decimal
                Arguments.of("1234.56", "1 234${AMOUNT_DELIMITER}56"),
                Arguments.of("1234567.89", "1 234 567${AMOUNT_DELIMITER}89"),

                // fraction
                Arguments.of("1.234", "1${AMOUNT_DELIMITER}23"),
                Arguments.of("1.2", "1${AMOUNT_DELIMITER}2"),
                Arguments.of("1.23", "1${AMOUNT_DELIMITER}23"),
                Arguments.of("1.2345", "1${AMOUNT_DELIMITER}23"),
                Arguments.of("1234.5678", "1 234${AMOUNT_DELIMITER}56"),
                Arguments.of(".345", "0${AMOUNT_DELIMITER}34"),
                Arguments.of("0.123", "0${AMOUNT_DELIMITER}12"),
            )
    }

    private val transformation = AmountInputTransformation()

    @ParameterizedTest
    @MethodSource("testData")
    fun `When transform Then expected`(input: String, expected: String) {
        // Given
        // When
        val actual = transformation.transform(input)
        // Then
        assertEquals(expected, actual)
    }
}
