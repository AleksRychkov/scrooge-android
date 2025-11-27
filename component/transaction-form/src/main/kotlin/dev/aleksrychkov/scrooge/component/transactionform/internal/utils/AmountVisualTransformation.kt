package dev.aleksrychkov.scrooge.component.transactionform.internal.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.aleksrychkov.scrooge.core.entity.formatAmount

private class AmountVisualTransformation(
    private val currency: String
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.trim()
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        val formattedText = "$currency ${originalText.formatAmount()}"
        return TransformedText(
            AnnotatedString(formattedText),
            AmountOffsetMapping(originalText, formattedText)
        )
    }
}

private class AmountOffsetMapping(
    private val originalText: String,
    private val formattedText: String
) : OffsetMapping {

    // Calculate how many prefix characters (e.g., "€ ")
    // were added before the actual number.
    private val prefixLength: Int =
        formattedText.indexOfFirst { it.isDigit() }.let { if (it >= 0) it else 0 }

    override fun originalToTransformed(offset: Int): Int {
        // We start after the prefix (e.g. currency + space)
        var transformedOffset = prefixLength
        var originalIndex = 0
        val formattedDigits = formattedText.drop(prefixLength)

        // Walk through the formatted text and count visible digits until
        // we reach the desired position in the original text.
        for (c in formattedDigits) {
            if (originalIndex >= offset) break
            if (c.isDigit()) {
                originalIndex++
            }
            transformedOffset++
        }
        return transformedOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= prefixLength) return 0

        var originalOffset = 0
        var transformedIndex = prefixLength
        val formattedDigits = formattedText.drop(prefixLength)

        // Map back from formatted → original
        for (c in formattedDigits) {
            if (transformedIndex >= offset) break
            if (c.isDigit()) {
                originalOffset++
            }
            transformedIndex++
        }
        return originalOffset.coerceAtMost(originalText.length)
    }
}

@Composable
internal fun rememberAmountVisualTransformation(currency: String): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current
    return remember(currency) {
        if (inspectionMode) {
            VisualTransformation.None
        } else {
            AmountVisualTransformation(currency = currency)
        }
    }
}
