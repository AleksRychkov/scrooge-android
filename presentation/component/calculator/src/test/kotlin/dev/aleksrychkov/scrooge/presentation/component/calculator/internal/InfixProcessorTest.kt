package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class InfixProcessorTest {

    @Nested
    inner class InputOpenParenthesis {
        @ParameterizedTest
        @CsvSource(
            // value added
            "'', '(', '('",
            "'(', '(', '(('",
            "'-', '(', '-('",
            "'+', '(', '+('",
            "'×', '(', '×('",
            "'÷', '(', '÷('",
            // value not added
            "'0', '(', '0'",
            "'1', '(', '1'",
            "',', '(', ','",
            "')', '(', ')'",
        )
        fun `process new infix`(infix: String, newValue: Char, expected: String) {
            val actual = processNewInfix(infix, newValue)
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class InputCloseParenthesis {
        @ParameterizedTest
        @CsvSource(
            "'()', ')', '()'",
            "'(()', ')', '(())'",
            "'(0', ')', '(0)'",
            "'(1', ')', '(1)'",
            "'-', ')', '-'",
            "'+', ')', '+'",
            "'×', ')', '×'",
            "'÷', ')', '÷'",
            "',', ')', ','",
            "'(', ')', '('",
            "'', ')', ''",
        )
        fun `process new infix`(infix: String, newValue: Char, expected: String) {
            val actual = processNewInfix(infix, newValue)
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class InputOperand {
        @ParameterizedTest
        @CsvSource(
            "')', '-', ')-'",
            "')', '+', ')+'",
            "')', '×', ')×'",
            "')', '÷', ')÷'",
            "'0', '-', '0-'",
            "'0', '+', '0+'",
            "'0', '×', '0×'",
            "'0', '÷', '0÷'",
            "'', '÷', ''",
            "'', '×', ''",
            "'', '-', '-'",
            "'', '+', '+'",
            "'(', '-', '(-'",
            "'(', '+', '(+'",
        )
        fun `process new infix`(infix: String, newValue: Char, expected: String) {
            val actual = processNewInfix(infix, newValue)
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class InputDelimiter {
        @ParameterizedTest
        @CsvSource(
            "'1', ',', '1,'",
            "'10', ',', '10,'",
            "'0', ',', '0,'",
            "'', ',', ''",
            "'10,0', ',', '10,0'",
            "'0,0', ',', '0,0'",
        )
        fun `process new infix`(infix: String, newValue: Char, expected: String) {
            val actual = processNewInfix(infix, newValue)
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class InputNaturalNumber {
        @ParameterizedTest
        @CsvSource(
            "'', '1', '1'",
            "'1,', '1', '1,1'",
            "'10', '1', '101'",
            "'(', '1', '(1'",
            "'-', '1', '-1'",
            "'+', '1', '+1'",
            "'×', '1', '×1'",
            "'÷', '1', '÷1'",
        )
        fun `process new infix`(infix: String, newValue: Char, expected: String) {
            val actual = processNewInfix(infix, newValue)
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class InputZero {
        @ParameterizedTest
        @CsvSource(
            "'', '0', '0'",
            "'1', '0', '10'",
            "'(', '0', '(0'",
            "'-', '0', '-0'",
            "'+', '0', '+0'",
            "'×', '0', '×0'",
            "'÷', '0', '÷0'",
            "',', '0', ',0'",
            "')', '0', ')'",
            "'0', '0', '0'",
            "'0,000', '0', '0,0000'",
        )
        fun `process new infix`(infix: String, newValue: Char, expected: String) {
            val actual = processNewInfix(infix, newValue)
            assertEquals(expected, actual)
        }
    }
}
