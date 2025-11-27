package dev.aleksrychkov.scrooge.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.aleksrychkov.scrooge.component.transactionform.internal.utils.rememberAmountVisualTransformation
import dev.aleksrychkov.scrooge.core.designsystem.composables.AppButton
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.DELIMITER
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormAmount(
    modifier: Modifier,
    amount: String,
    currency: String,
    amountChanged: (String) -> Unit,
    openCurrencyModal: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(Normal),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val focusManager = LocalFocusManager.current
        val amountFormatter = rememberAmountVisualTransformation(currency = currency)
        TextField(
            modifier = Modifier.weight(weight = 1f, fill = true),
            value = amount,
            singleLine = true,
            label = {
                Text(text = stringResource(Resources.string.amount))
            },
            placeholder = {
                val placeholder = amount.ifBlank { "$currency 0${DELIMITER}00" }
                Text(text = placeholder)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            onValueChange = { value ->
                amountChanged(value)
            },
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Unspecified,
            ),
            visualTransformation = amountFormatter,
        )
        Spacer(
            modifier = Modifier.width(Normal)
        )

        AppButton(
            modifier = Modifier.height(IntrinsicSize.Max),
            onClick = openCurrencyModal,
        ) {
            Text(text = currency)
        }
    }
}
