package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.runtime.Stable
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER_STRING

@Stable
internal class AmountInputTransformation : InputTransformation {

    private companion object {
        val regex = Regex("[^0-9,]")
        const val TWO_DECIMALS_LIMIT = 3
    }

    @Suppress("ReturnCount")
    override fun TextFieldBuffer.transformInput() {
        if (length <= 0) return

        onlyDigitAndDelimiter()

        if (length == 0) return

        padZeroIfNeeded()
        deleteLeadingZeros()

        if (length == 0) return

        val firstDelimiterIndex = asCharSequence().indexOf(AMOUNT_DELIMITER)
        if (firstDelimiterIndex < 0) return

        deleteExtraDelimiters(firstDelimiterIndex)

        if (length == 0) return

        atMostTwoDecimals(firstDelimiterIndex)
    }

    private fun TextFieldBuffer.onlyDigitAndDelimiter() {
        while (true) {
            val index = regex.find(asCharSequence()) ?: break
            val char = charAt(index.range.first)
            if (char == '.') {
                replace(index.range.first, index.range.first + 1, AMOUNT_DELIMITER_STRING)
            } else {
                delete(index.range.first, index.range.first + 1)
            }
        }
    }

    private fun TextFieldBuffer.padZeroIfNeeded() {
        if (charAt(0) == AMOUNT_DELIMITER) {
            insert(0, "0")
        }
    }

    private fun TextFieldBuffer.deleteLeadingZeros() {
        while (length > 1 && charAt(0) == '0' && charAt(1) != AMOUNT_DELIMITER) {
            delete(0, 1)
        }
    }

    private fun TextFieldBuffer.deleteExtraDelimiters(firstDelimiterIndex: Int) {
        while (true) {
            val lastDelimiterIndex = asCharSequence()
                .lastIndexOf(AMOUNT_DELIMITER)
            if (lastDelimiterIndex == -1 || lastDelimiterIndex == firstDelimiterIndex) {
                break
            }
            delete(lastDelimiterIndex, length)
        }
    }

    private fun TextFieldBuffer.atMostTwoDecimals(firstDelimiterIndex: Int) {
        if (length - firstDelimiterIndex > TWO_DECIMALS_LIMIT) {
            delete(firstDelimiterIndex + TWO_DECIMALS_LIMIT, length)
        }
    }
}
