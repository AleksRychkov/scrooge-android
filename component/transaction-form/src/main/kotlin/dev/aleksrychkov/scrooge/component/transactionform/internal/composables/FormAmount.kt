package dev.aleksrychkov.scrooge.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.component.transactionform.internal.utils.rememberAmountVisualTransformation
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
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
    DsSecondaryCard(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Max),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                colors = DsInputTextFieldsColors(),
                visualTransformation = amountFormatter,
            )

            TextButton(
                modifier = Modifier.height(IntrinsicSize.Max),
                onClick = openCurrencyModal,
            ) {
                Text(
                    text = currency,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormAmount(
                modifier = Modifier,
                amount = "12312,00",
                currency = CurrencyEntity.RUB.currencySymbol,
                amountChanged = {},
                openCurrencyModal = {},
            )
        }
    }
}
