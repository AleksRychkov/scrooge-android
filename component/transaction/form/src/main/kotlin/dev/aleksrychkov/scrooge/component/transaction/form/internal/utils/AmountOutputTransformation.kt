package dev.aleksrychkov.scrooge.component.transaction.form.internal.utils

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.runtime.Stable
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER

@Stable
internal data class AmountOutputTransformation(
    private val currency: String
) : OutputTransformation {

    private companion object {
        const val PAD_LENGTH = 3
    }

    override fun TextFieldBuffer.transformOutput() {
        if (length > 0) {
            val value = asCharSequence()
            val intPartLength = if (value.contains(AMOUNT_DELIMITER)) {
                value.take(value.indexOf(AMOUNT_DELIMITER)).length
            } else {
                value.length
            }
            var index = intPartLength - PAD_LENGTH
            while (index > 0) {
                insert(index, " ")
                index -= PAD_LENGTH
            }

            insert(0, "$currency ")
        }
    }
}
