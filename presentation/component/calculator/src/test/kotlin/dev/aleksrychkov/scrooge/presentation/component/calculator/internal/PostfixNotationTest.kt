package dev.aleksrychkov.scrooge.presentation.component.calculator.internal

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PostfixNotationTest {
    @ParameterizedTest
    @CsvSource(
        "1.5,                       1.5",
        "1.5+2,                     3.5",
        "3+4.25,                    7.25",
        "2.5*3,                     7.5",
        "10/2.5,                    4.0",
        "1.2+2*3.5,                 8.2",
        "(1.5+2.5)*3,               12.0",
        "3*(2.5+4),                 19.5",
        "3.5*(2+(4.25-1)),          18.375",
        "10.0/5.0/2,                1.0",
        "10/(5.0/2.5),              5.0",
        "1.1+2.2*3.3/4.4-5.5,       -2.75",
        "6.0-(2.5*2),               1.0",
        "(8.2/(4.1-2)),             3.904761904761905",
        "7*(3.5-1.5)/2,             7.0",
        "4+(2.25*3.75),             12.4375",
        "9/(3.0/3),                 9.0",
        "1.0+2.0+(3.0*4.0),         15.0",
        "(1.25*(2+3.75))-4,         3.1875",
        "5.5/(1.1+0.55),            3.3333333333333335",
        "(2.5+3.5)/(1.2*0.5),       10.0",
        "-10,                       -10.0",
        "-10-10,                    -20.0",
        "(-10-10),                  -20.0",
    )
    fun `infix to calculated result`(infix: String, expected: Float) {
        val transformer = InfixCalculator()
        val actual = transformer.calculate(infix)
        val epsilon = 1e-5f
        assertEquals(expected, actual, epsilon)
    }
}
