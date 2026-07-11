package dev.aleksrychkov.scrooge.core.utils

import kotlin.math.abs
import kotlin.math.round

fun formatCompactNumber(value: Double): String {
    val unit = COMPACT_UNITS.firstOrNull { abs(value) >= it.threshold }
        ?: return value.toReadableNumber()
    return (value / unit.threshold).toReadableNumber() + unit.suffix
}

private fun Double.toReadableNumber(): String {
    val rounded = round(this * DECIMAL_PRECISION) / DECIMAL_PRECISION
    return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else rounded.toString()
}

private data class CompactUnit(val threshold: Double, val suffix: String)

private val COMPACT_UNITS = listOf(
    CompactUnit(TRILLION, "T"),
    CompactUnit(BILLION, "B"),
    CompactUnit(MILLION, "M"),
    CompactUnit(THOUSAND, "K"),
)

private const val DECIMAL_PRECISION = 10
private const val THOUSAND = 1_000.0
private const val MILLION = THOUSAND * THOUSAND
private const val BILLION = MILLION * THOUSAND
private const val TRILLION = BILLION * THOUSAND
